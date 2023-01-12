package com.example.kmmtestapplication.message.data

import com.example.kmmtestapplication.message.domain.Message
import kotlinx.coroutines.flow.Flow

/**
 * A service for centralized message showing
 */
interface MessageService {

    val messageFlow: Flow<Message>

    fun showMessage(message: Message)
}