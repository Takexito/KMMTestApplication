package com.example.kmmtestapplication.flow

import com.example.kmmtestapplication.flow.NativeError

/**
 * Represents an error in a way that the specific platform is able to handle
 */
actual class NativeError

/**
 * Converts a [Throwable] to a [NativeError].
 */
internal actual fun Throwable.asNativeError(): NativeError {
    TODO("Not yet implemented")
}