package com.example.kmmtestapplication.network

import io.ktor.client.statement.*

interface SuccessErrorHandler {
    fun handle(response: HttpResponse)
}