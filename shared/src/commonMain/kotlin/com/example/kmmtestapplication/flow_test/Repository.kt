package com.example.kmmtestapplication.flow_test

import com.example.kmmtestapplication.flow.Optional
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class Repository {
    private val stateFlow: MutableStateFlow<Float> = MutableStateFlow(100.100F)
    private val sharedPlayerFlow: MutableSharedFlow<Player> = MutableSharedFlow(3, 10, BufferOverflow.DROP_OLDEST)
    private val sharedPlayerOptionalFlow: MutableSharedFlow<Optional<Player>> = MutableSharedFlow(3, 10, BufferOverflow.DROP_OLDEST)
    fun getData(): MutableStateFlow<Float> = stateFlow
    fun getPlayer(): MutableSharedFlow<Player> = sharedPlayerFlow
    fun getPlayerOptional(): MutableSharedFlow<Optional<Player>> = sharedPlayerOptionalFlow

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

        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            sharedPlayerOptionalFlow.emit(Optional.Some(Player("123", 213, Score(100.112F, 12F))))
            delay(500)
            sharedPlayerOptionalFlow.emit(Optional.None())
            delay(700)
            sharedPlayerOptionalFlow.emit(Optional.Some(Player("3213", 3645, Score(100.112F, 12F))))
            delay(500)
            sharedPlayerOptionalFlow.emit(Optional.Some(Player("1221312fds3", 213, Score(100.112F, 12F))))
            delay(1500)
            sharedPlayerOptionalFlow.emit(Optional.Some(Player("dsfsdf", 213, Score(100.112F, 12F))))
        }
    }
}