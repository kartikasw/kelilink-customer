package com.kartikasw.kelilink.core.data.helper

import com.kartikasw.kelilink.BuildConfig

object Constants {

    const val CONTENT_TYPE = "application/json"
    const val SERVER_KEY = BuildConfig.SERVER_KEY

    const val CHANNEL_ID = "kelilink_channel"
    const val CHANNEL_NAME = "kelilink"

    const val PREFERENCE_NAME = "Kelilink_Preference"
    const val DATABASE_NAME = "Kelilink.db"

    object PreferenceValue {
        const val FCM_TOKEN = "fcm_token"
    }

    object Extra {
        const val EXTRA_INVOICE_ID = "invoice_id"
        const val EXTRA_STORE_ID = "store_id"
        const val EXTRA_STORE_CATEGORY = "store_id"
        const val EXTRA_STORE_NAME = "store_name"
        const val EXTRA_MENU_ID = "menu_id"
        const val EXTRA_MENU_NOTE = "menu_note"
        const val EXTRA_USER_LATITUDE = "user_latitude"
        const val EXTRA_USER_LONGITUDE = "user_longitude"
        const val EXTRA_USER_NAME = "user_name"
        const val EXTRA_USER_PHONE_NUMBER = "user_phone_number"
        const val EXTRA_TOTAL_PRICE = "total_price"
        const val EXTRA_EMAIL = "email"
    }

    object DatabaseCollection {
        const val USER_COLLECTION = "user"
        const val STORE_COLLECTION = "store"
        const val MENU_COLLECTION = "menu"
        const val INVOICE_COLLECTION = "invoice"
        const val ORDERS_COLLECTION = "orders"
    }

    object DatabaseColumn {
        const val UID_COLUMN = "uid"
        const val EMAIL_COLUMN = "email"
        const val FCM_TOKEN_COLUMN = "fcm_token"
        const val NAME_COLUMN = "name"
        const val PHONE_NUMBER_COLUMN = "phone_number"

        const val ID_COLUMN = "id"
        const val ADDRESS_COLUMN = "address"
        const val CATEGORIES_COLUMN = "categories"
        const val IMAGE_COLUMN = "image"
        const val LATITUDE_COLUMN = "lat"
        const val LONGITUDE_COLUMN = "lon"
        const val OPERATING_STATUS_COLUMN = "operating_status"
        const val QUEUE_COLUMN = "queue"
        const val TOTAL_QUEUE_COLUMN = "total_queue"

        const val STORE_ID_COLUMN = "store_id"
        const val AVAILABLE_COLUMN = "status"
        const val DESCRIPTION_COLUMN = "description"
        const val PRICE_COLUMN = "price"
        const val UNIT_COLUMN = "unit"

        const val AMOUNT_COLUMN = "amount"
        const val NOTE_COLUMN = "note"
        const val TOTAL_PRICE_COLUMN = "total_price"

        const val STATUS_COLUMN = "status"
        const val TIME_COLUMN = "time"
        const val USER_ID_COLUMN = "user_id"
        const val QUEUE_ORDER_COLUMN = "queue_order"
    }

    object Table {
        const val USER_TABLE = "user_table"
        const val MENU_TABLE = "menu_table"
        const val STORE_TABLE = "store_table"
    }

    object InvoiceStatus {
        const val WAITING = "waiting"
        const val COOKING = "cooking"
        const val READY = "ready"
        const val DONE = "done"
        const val DECLINED = "declined"
        const val MISSED = "missed"
    }

}