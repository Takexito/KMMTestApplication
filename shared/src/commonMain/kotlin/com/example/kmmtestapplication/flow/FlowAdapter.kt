package com.example.kmmtestapplication.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

sealed class Optional<T : Any> {
    class Some<T : Any>(val value: T) : Optional<T>()
    class None<T : Any> : Optional<T>()

    fun get(): T? {
        return if (this is None) null else (this as Some).value
    }
}

class FlowAdapter<T : Any>(
    val scope: CoroutineScope,
    val flow: Flow<T>
) {
    fun subscribe(
        onEach: (item: T) -> Unit,
        onComplete: () -> Unit,
        onThrow: (error: Throwable) -> Unit
    ): Canceller = JobCanceller(
        flow.onEach { onEach(it) }
            .catch { onThrow(it) }
            .onCompletion { onComplete() }
            .launchIn(scope)
    )
}

interface Canceller {
    fun cancel()
}

private class JobCanceller(private val job: Job) : Canceller {
    override fun cancel() {
        job.cancel()
    }
}