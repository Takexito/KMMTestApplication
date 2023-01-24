package com.example.kmmtestapplication.flow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.typeOf

typealias TMutableStateFlow<T> = () -> CMutableStateFlow<T>
typealias TStateFlow<T> = () -> CStateFlow<T>

inline fun <reified T> TMutableStateFlow(stateFlow: MutableStateFlow<T>): TMutableStateFlow<T> =
    { CMutableStateFlow(stateFlow, typeOf<T>()) }

val<T> TStateFlow<T>.value: T get() {
    return this().value
}

suspend fun<T> TMutableStateFlow<T>.emit(value: T){
    this().emit(value)
}

fun<T> TMutableStateFlow<T>.tryEmit(value: T){
    this().tryEmit(value)
}