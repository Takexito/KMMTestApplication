package com.example.kmmtestapplication.error

import com.example.kmmtestapplication.Build

/**
 * Returns human readable messages for exceptions.
 */
val Exception.errorMessage: String
    get() = when (this) {
        is ServerException, is DeserializationException -> "invalid responce"

        is NoServerResponseException -> "no server response"

        is NoInternetException -> "no internet connection"

        is SSLHandshakeException -> "ssl handshake"

        is ExternalAppNotFoundException -> "external app not found"

        else -> {
            val description = this.message
            if (description != null && Build.isDebug) {
                "Unexpected error. $description"
            } else {
                "Unexpected error."
            }
        }
    }

