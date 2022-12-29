package com.example.kmmtestapplication.property

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty0

val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

@Suppress("UNCHECKED_CAST")
val <T> KProperty0<T>.flow: StateFlow<T>
    get() = this.get() as? StateFlow<T>
        ?: throw IllegalArgumentException("Property $name is not observable. Property must be a StateFlow")

fun <T, R> computed(
    property: KProperty0<StateFlow<T>>,
    transform: (T) -> R
): StateFlow<R> {
    val flow = property.get()
    val initialValue = flow.value
    val resultFlow = MutableStateFlow(transform(initialValue))
    scope.launch {
        flow.dropWhile {
                it == initialValue
            }
            .collect {
                resultFlow.value = transform(it)
            }
    }
    return resultFlow
}


fun <T1, T2, R> computed(
    property1: KProperty0<StateFlow<T1>>,
    property2: KProperty0<StateFlow<T2>>,
    transform: (T1, T2) -> R
): StateFlow<R> {
    return computedImpls(property1, property2) { args: List<*> ->
        transform(
            args[0] as T1,
            args[1] as T2
        )
    }
}

fun <T1, T2, T3, R> computed(
    property1: KProperty0<StateFlow<T1>>,
    property2: KProperty0<StateFlow<T2>>,
    property3: KProperty0<StateFlow<T3>>,
    transform: (T1, T2, T3) -> R
): StateFlow<R> {
    return computedImpls(property1, property2, property3) { args: List<*> ->
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3
        )
    }
}

private inline fun <T, R> computedImpls(
    vararg properties: KProperty0<T>,
    crossinline transform: (List<T>) -> R
): StateFlow<R> {

    val flows = properties.map { it.flow }
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