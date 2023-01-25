package com.example.kmmtestapplication.flow_test

import com.example.kmmtestapplication.flow.CMutableStateFlow
import com.example.kmmtestapplication.flow.Optional
import com.example.kmmtestapplication.property.scope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ViewModel {
    val cStateFlow: CMutableStateFlow<Optional<Int>> =
        CMutableStateFlow(MutableStateFlow(Optional(0)))

    init {
        scope.launch {
            delay(500)
            cStateFlow.emit(Optional(10))
            delay(1000)
            cStateFlow.emit(Optional(101))
            delay(1000)
            cStateFlow.emit(Optional(null))
        }
    }

}


