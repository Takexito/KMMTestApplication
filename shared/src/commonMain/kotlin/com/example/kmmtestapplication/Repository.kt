package com.example.kmmtestapplication

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class Repository {
    private val stateFlow: MutableStateFlow<Float> = MutableStateFlow(100.100F)
    private val sharedPlayerFlow: MutableSharedFlow<Player> = MutableSharedFlow(3, 10, BufferOverflow.DROP_OLDEST)
    fun getData(): MutableStateFlow<Float> = stateFlow
    fun getPlayer(): MutableSharedFlow<Player> = sharedPlayerFlow

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            delay(500)
            stateFlow.emit(0.5F)
            delay(100)
            stateFlow.emit(20.0F)
            delay(1500)
            stateFlow.emit(30.56F)
        }

        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            sharedPlayerFlow.emit(Player("123", 213, Score(100.112F, 12F)))
            sharedPlayerFlow.emit(Player("3213", 3645, Score(100.112F, 12F)))
            delay(500)
            sharedPlayerFlow.emit(Player("1221312fds3", 213, Score(100.112F, 12F)))
            delay(1500)
            sharedPlayerFlow.emit(Player("dsfsdf", 213, Score(100.112F, 12F)))
        }
    }
}