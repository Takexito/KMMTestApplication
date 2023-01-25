package com.example.kmmtestapplication.flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CMutableStateFlow<T : Any>(
    private val flow: MutableStateFlow<T>,
) : CStateFlow<T>(flow), MutableStateFlow<T> {

    constructor(value: T) : this(MutableStateFlow(value))

    override var value: T
        get() = super.value
        set(value) {
            flow.value = value
        }

    override val subscriptionCount: StateFlow<Int> = flow.subscriptionCount

    override suspend fun emit(value: T) = flow.emit(value)

    @ExperimentalCoroutinesApi
    override fun resetReplayCache() = flow.resetReplayCache()

    override fun tryEmit(value: T): Boolean = flow.tryEmit(value)

    override fun compareAndSet(expect: T, update: T): Boolean = flow.compareAndSet(expect, update)
}

fun <T : Any> CMutableStateFlow<Optional<T>>.updateValue(value: T?) {
    this.value = value.asOptional()
}