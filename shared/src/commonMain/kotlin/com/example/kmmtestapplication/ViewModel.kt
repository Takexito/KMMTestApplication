package com.example.kmmtestapplication

import kotlinx.coroutines.flow.*

data class Player(
    val name: String,
    val age: Int,
    val score: Score
)

data class Score(
    val maxScore: Float,
    val currentScore: Float
)


class ViewModel(private val repository: Repository) {
    val flows: Flows<Player>
    get() = Flows(player, player.asNativeFlow())

    val data: MutableStateFlow<Float>
        get() = repository.getData()

    val nativeFlow: NativeFlow<Float>
        get() = data.asNativeFlow()

    val player: MutableSharedFlow<Player>
        get() = repository.getPlayer()

    val playerNativeFlow: NativeFlow<Player>
        get() = player.asNativeFlow()

    fun returnNativeFlow(): NativeFlow<Float> {
        return data.asNativeFlow()
    }
}


