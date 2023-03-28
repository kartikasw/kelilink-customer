package com.kartikasw.kelilink.util.params

data class RegisterParam(
    val email: String,
    val password: String,
    val user: MutableMap<String, Any>
)