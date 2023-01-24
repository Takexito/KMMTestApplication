package com.example.kmmtestapplication.flow_test

import com.example.kmmtestapplication.flow.TMutableStateFlow
import com.example.kmmtestapplication.property.scope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ViewModel {
    val cStateFlow: TMutableStateFlow<Int?> = TMutableStateFlow(MutableStateFlow(0))

    init {
        scope.launch {
            delay(500)
            cStateFlow().emit(10)
            delay(1000)
            cStateFlow().emit(101)
            delay(1000)
            cStateFlow().emit(null)
        }
    }

}


