package com.example.kmmtestapplication.flow

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

open class CStateFlow<T : Any>(private val flow: StateFlow<T>) : StateFlow<T> {
    override val replayCache: List<T> = flow.replayCache

    override suspend fun collect(collector: FlowCollector<T>): Nothing = flow.collect(collector)

    override val value: T
        get() = flow.value
}

fun <T : Any> StateFlow<T>.asCStateFlow(): CStateFlow<T> = CStateFlow(this)

fun <T : Any> CStateFlow<Optional<T>>.nullableValue(): T? = this.value.get()