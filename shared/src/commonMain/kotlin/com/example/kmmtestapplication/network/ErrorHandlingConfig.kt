package com.example.kmmtestapplication.network

import com.example.kmmtestapplication.debug_tools.DebugTools
import com.example.kmmtestapplication.error.*
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.utils.io.errors.*

fun <T : HttpClientEngineConfig> HttpClientConfig<T>.setDefaultErrorHandler(
    successErrorHandler: SuccessErrorHandler? = null,
    debugTools: DebugTools? = null
) {
    HttpResponseValidator {
        validateResponse { response ->
            if (response.status.isSuccess()) {
                successErrorHandler?.handle(response)
                return@validateResponse
            }
            val exception = mapToFailureException(response)
            debugTools?.collectNetworkError(exception)
            throw exception
        }

        handleResponseExceptionWithRequest { cause, _ ->
            val exception = mapToFailureException(cause)
            debugTools?.collectNetworkError(exception)
            throw exception
        }
    }
}

private fun mapToFailureException(response: HttpResponse) = when (response.status) {
    HttpStatusCode.GatewayTimeout, HttpStatusCode.ServiceUnavailable -> NoServerResponseException(
        ClientRequestException(response, response.toString())
    )
    HttpStatusCode.Unauthorized -> UnauthorizedException(
        ClientRequestException(
            response,
            response.toString()
        )
    )
    else -> ServerException(ClientRequestException(response, response.toString()))
}

private fun mapToFailureException(throwable: Throwable) = when (throwable) {
    is ApplicationException -> throwable
    is ContentConvertException -> DeserializationException(throwable)
    is SocketTimeoutException -> NoServerResponseException(throwable)
    is Exception -> NoServerResponseException(throwable)
    is IOException -> (throwable.cause as? ApplicationException)
        ?: NoInternetException(throwable)
    else -> UnknownException(throwable, throwable.message ?: "Unknown")
}