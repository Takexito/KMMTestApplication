package com.example.kmmtestapplication

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Represents an error in a way that the specific platform is able to handle
 */
expect class NativeError

/**
 * Converts a [Throwable] to a [NativeError].
 */
internal expect fun Throwable.asNativeError(): NativeError

class Flows<T>(val flow: MutableSharedFlow<T>, val nativeFlow: NativeFlow<T>)

typealias NativeCancellable = () -> Any

internal inline fun Job.asNativeCancellable(): NativeCancellable = { cancel() }

typealias NativeCallback<T> = (T) -> Any

typealias NativeFlow<T> = (onItem: NativeCallback<T>, onComplete: NativeCallback<NativeError?>) -> NativeCancellable

fun <T> Flow<T>.asNativeFlow(scope: CoroutineScope? = null): NativeFlow<T> {
    val coroutineScope = scope ?: CoroutineScope(SupervisorJob() + Dispatchers.Main)
    return (collect@{ onItem: NativeCallback<T>, onComplete: NativeCallback<NativeError?> ->
        val job = coroutineScope.launch {
            try {
                collect { onItem(it) }
                onComplete(null)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                onComplete(e.asNativeError())
            }
        }
        job.invokeOnCompletion { cause ->
            if (cause !is CancellationException) return@invokeOnCompletion
            onComplete(cause.asNativeError())
        }
        return@collect job.asNativeCancellable()
    })
}

