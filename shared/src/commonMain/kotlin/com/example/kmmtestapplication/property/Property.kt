package com.example.kmmtestapplication.property

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty0

val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)


@Suppress("UNCHECKED_CAST")
val <T> KProperty0<T>.flows: StateFlow<T>
    get() = this.get() as? StateFlow<T>
        ?: throw IllegalArgumentException("Property $name is not observable. Make sure you use state, stateFromFlow or computed to create it.")

fun <T1, T2, R> computed(
    property1: KProperty0<MutableStateFlow<T1>>,
    property2: KProperty0<MutableStateFlow<T2>>,
    transform: (T1, T2) -> R
): MutableStateFlow<R> {
    return computedImpls(property1, property2) { args: List<*> ->
        transform(
            args[0] as T1,
            args[1] as T2
        )
    }
}

private inline fun <T, R> computedImpls(
    vararg properties: KProperty0<T>,
    crossinline transform: (List<T>) -> R
): MutableStateFlow<R> {

    val flows = properties.map { it.flows }
    val initialValues = flows.map { it.value }
    val elementsFlow = MutableStateFlow(initialValues)
    val resultFlow = MutableStateFlow(transform(initialValues))

    flows.forEachIndexed { index, flow ->
        scope.launch {
            flow
                .dropWhile {
                    it == initialValues[index]
                }
                .collect {
                    elementsFlow.value =
                        elementsFlow.value.toMutableList().apply { this[index] = it }
                }
        }
    }

    scope.launch {
        elementsFlow
            .dropWhile {
                it == initialValues
            }
            .collect {
                resultFlow.value = transform(it)
            }
    }

    return resultFlow
}