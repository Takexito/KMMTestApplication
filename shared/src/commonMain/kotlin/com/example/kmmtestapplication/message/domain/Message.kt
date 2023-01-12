package com.example.kmmtestapplication.message.domain

data class Message(
    val text: String,
    val iconRes: Int? = null,
    val actionTitle: String? = null,
    val action: (() -> Unit)? = null
)