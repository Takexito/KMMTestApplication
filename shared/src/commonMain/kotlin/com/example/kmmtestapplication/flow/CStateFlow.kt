package com.example.kmmtestapplication.flow

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KType

open class CStateFlow<T>(private val flow: StateFlow<T>, val type: KType) : StateFlow<T> {
    override val replayCache: List<T> = flow.replayCache

    override suspend fun collect(collector: FlowCollector<T>): Nothing = flow.collect(collector)

    override val value: T = flow.value

    private inline fun <reified T> isMarkedNullable() = null is T
}