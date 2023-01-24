package com.example.kmmtestapplication.flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KType

class CMutableStateFlow<T>(
    private val flow: MutableStateFlow<T>,
    type: KType
) : CStateFlow<T>(flow, type), MutableStateFlow<T> {

    constructor(value: T, type: KType): this(MutableStateFlow(value), type)

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