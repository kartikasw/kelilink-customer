package com.example.kelilink.core.data.source.remote.service.firebase

import android.util.Log
import com.example.kelilink.core.data.helper.Constants.DatabaseCollection.USER_COLLECTION
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.ID_COLUMN
import com.example.kelilink.core.data.helper.Constants.DatabaseColumn.TIME_COLUMN
import com.example.kelilink.core.data.helper.Response
import com.example.kelilink.core.data.source.remote.response.UserResponse
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

abstract class FirebaseService {

    companion object {
        const val TAG = "FirebaseService"
    }

    private val auth = FirebaseAuth.getInstance()

    fun getUser() = auth.currentUser

    val firestore = Firebase.firestore

    fun signOut(): Unit = auth.signOut()

    fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Response<String>> =
        flow {
            val createUser = auth
                .createUserWithEmailAndPassword(email, password)
                .await()

            val uid = createUser.user?.uid
            createUser.user!!.sendEmailVerification()
            signOut()

            if (uid != null) {
                emit(Response.Success(uid))
            } else {
                emit(Response.Empty)
            }
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Response<String>> =
        flow {
            val createUser = auth
                .signInWithEmailAndPassword(email, password)
                .await()

            val user = createUser.user

            if (user!!.isEmailVerified) {
                emit(Response.Success(user.uid))
            } else {
                signOut()
                emit(Response.Error("Email belum diverifikasi"))
            }
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun sendPasswordResetEmail(email: String): Flow<Response<Unit>> =
        flow <Response<Unit>>{
            auth.sendPasswordResetEmail(email).await()
            emit(Response.Success(Unit))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun reAuthenticate(
        oldPassword: String,
        newPassword: String
    ): Flow<Response<UserResponse>> =
        flow {
            val email = getUser()!!.email.toString()
            val credential = EmailAuthProvider
                .getCredential(email, oldPassword)

            var isComplete = false

            getUser()!!.reauthenticate(credential)
                .addOnCompleteListener {
                    isComplete = true
                }.await()

            if(isComplete) {
                emitAll(updatePassword(newPassword))
            } else {
                emit(Response.Empty)
            }

        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)


    private fun updatePassword(
        newPassword: String
    ): Flow<Response<UserResponse>> =
        flow {
            val userId = getUser()!!.uid
            getUser()!!.updatePassword(newPassword).await()

            emitAll(getDocumentById<UserResponse>(USER_COLLECTION, userId))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> setDocument(
        collection: String,
        docId: String,
        document: Any
    ): Flow<Response<ResponseType>> =
        flow {
            firestore
                .collection(collection)
                .document(docId)
                .set(document)
                .await()

            emitAll(getDocumentById<ResponseType>(collection, docId))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> addDocument(
        collection: String,
        document: Any
    ): Flow<Response<ResponseType>> =
        flow {
            val result = firestore
                .collection(collection)
                .add(document)
                .await()

            val id = result.id

            emitAll(updateFieldInDocument<ResponseType>(collection, id, ID_COLUMN, id))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> setDocumentInSubCollection(
        collection: String,
        docId: String,
        subCollection: String,
        list: List<Any>
    ): Flow<Response<ResponseType>> =
        flow {
            Log.d(TAG, "1")
            firestore.runBatch {
                list.forEach {
                    firestore
                        .collection(collection)
                        .document(docId)
                        .collection(subCollection)
                        .add(it)
                    Log.d(TAG, "11")

                }
                Log.d(TAG, "111")

            }.await()

            emitAll(getDocumentById<ResponseType>(collection, docId))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun addValueToArrayOfDocument(
        collection: String,
        docId: String,
        fieldName: String,
        value: String
    ): Flow<Response<Unit>> =
        flow <Response<Unit>>{
            firestore
                .collection(collection)
                .document(docId)
                .update(fieldName, FieldValue.arrayUnion(value))
                .await()
            emit(Response.Success(Unit))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)


    /*
    *
    *
    Get data
    *
    *
     */
    inline fun <reified ResponseType> getDocumentById(
        collection: String,
        docId: String
    ): Flow<Response<ResponseType>> =
        flow {
            val result = firestore
                .collection(collection)
                .document(docId)
                .get()
                .await()

            if (result.exists()) {
                emit(Response.Success(result.toObject(ResponseType::class.java)!!))
            } else {
                emit(Response.Empty)
            }
        }.catch {
            Log.e(TAG, it.message.toString())
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> getDocumentByField(
        collection: String,
        field: String,
        value: Any
    ): Flow<Response<List<ResponseType>>> =
        flow {
            val result = firestore
                .collection(collection)
                .whereEqualTo(field, value)
                .get()
                .await()

            if (result.isEmpty) {
                emit(Response.Empty)
            } else {
                emit(Response.Success(result.toObjects(ResponseType::class.java)))
            }
        }.catch {
            Log.e(TAG, it.message.toString())
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> getDocumentByField(
        collection: String,
        field: String,
        value: List<Any>
    ): Flow<Response<List<ResponseType>>> =
        flow {
            val result = firestore
                .collection(collection)
                .whereIn(field, value)
                .get()
                .await()

            if (result.isEmpty) {
                emit(Response.Empty)
            } else {
                emit(Response.Success(result.toObjects(ResponseType::class.java)))
            }
        }.catch {
            Log.e(TAG, it.message.toString())
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> getDocumentInSubCollection(
        collection: String,
        subCollection: String,
        docId: String
    ): Flow<Response<List<ResponseType>>> =
        flow {
            val result = firestore
                .collection(collection)
                .document(docId)
                .collection(subCollection)
                .get()
                .await()

            if (result.isEmpty) {
                emit(Response.Empty)
            } else {
                emit(Response.Success(result.toObjects(ResponseType::class.java)))
            }
        }.catch {
            Log.e(TAG, it.message.toString())
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> getDocumentByFieldAndOrderByTime(
        collection: String,
        field: String,
        value: Any
    ): Flow<Response<List<ResponseType>>> =
        flow {
            val result = firestore
                .collection(collection)
                .whereEqualTo(field, value)
                .orderBy(TIME_COLUMN, Query.Direction.DESCENDING)
                .get()
                .await()

            if (result.isEmpty) {
                emit(Response.Empty)
            } else {
                emit(Response.Success(result.toObjects(ResponseType::class.java)))
            }
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> getDocumentByArrayValue(
        collection: String,
        field: String,
        value: Any
    ): Flow<Response<List<ResponseType>>> =
        flow {
            val result = firestore
                .collection(collection)
                .whereArrayContains(field, value)
                .get()
                .await()

            if (result.isEmpty) {
                emit(Response.Empty)
            } else {
                emit(Response.Success(result.toObjects(ResponseType::class.java)))
            }
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    /*
    *
    *
    Update data
    *
    *
     */
    inline fun <reified ResponseType> updateDocument(
        collection: String,
        docId: String,
        value: MutableMap<String, Any>
    ): Flow<Response<ResponseType>> =
        flow {
            firestore
                .collection(collection)
                .document(docId)
                .update(value)
                .await()

            emitAll(getDocumentById<ResponseType>(collection, docId))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> updateFieldInDocument(
        collection: String,
        docId: String,
        fieldName: String,
        value: Any
    ): Flow<Response<ResponseType>> =
        flow {
            firestore
                .collection(collection)
                .document(docId)
                .update(fieldName,value)
                .await()

            emitAll(getDocumentById<ResponseType>(collection, docId))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    /*
   *
   *
   Delete data
   *
   *
    */
    fun deleteDocument(
        collection: String,
        docId: String
    ): Flow<Response<Unit>> =
        flow <Response<Unit>>{
            firestore
                .collection(collection)
                .document(docId)
                .delete()
                .await()

            emit(Response.Success(Unit))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    /*
    *
    *  LISTEN TO DATA
    *
    */
    fun Query.snapshotFlow(): Flow<QuerySnapshot> =
        callbackFlow {
            val listenerRegistration = addSnapshotListener { value, error ->
                if (error != null) {
                    close()
                    return@addSnapshotListener
                }
                if (value != null)
                    trySend(value)
            }

            awaitClose {
                listenerRegistration.remove()
            }
        }
}