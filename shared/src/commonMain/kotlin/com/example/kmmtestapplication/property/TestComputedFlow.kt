package com.example.kmmtestapplication.property

import com.example.kmmtestapplication.flow.asNativeFlow
import com.example.kmmtestapplication.property.computed
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

class TestComputedFlow {
    private val stateFloatFlow: MutableStateFlow<Float?> = MutableStateFlow(100.100F)
    private val stateStringFlow: MutableStateFlow<String?> = MutableStateFlow("dfsdfd")

    val stateFlow = computed(
        ::stateStringFlow,
        ::stateFloatFlow,
    ) { string, float ->
        if (string == null || float == null) return@computed null
        string + float
    }

    val nativeFlow = stateFlow.asNativeFlow()

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            stateFloatFlow.emit(11111F)
            delay(500)
            stateFloatFlow.emit(11F)
            delay(500)
            stateFloatFlow.emit(null)
            delay(1500)
            stateFloatFlow.emit(4534543F)
        }

        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            stateStringFlow.emit("safadfgfsg")
            delay(100)
            stateStringFlow.emit("dfdsfds")
            delay(660)
            stateStringFlow.emit("ereluikuiy")
            delay(1000)
            stateStringFlow.emit("oiuoiuy")
        }
    }
}