package com.example.kmmtestapplication.flow_test

import com.example.kmmtestapplication.flow.FlowAdapter
import com.example.kmmtestapplication.flow.NativeFlow
import com.example.kmmtestapplication.flow.Optional
import com.example.kmmtestapplication.flow.asNativeFlow
import com.example.kmmtestapplication.property.scope
import kotlinx.coroutines.flow.MutableSharedFlow

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

    // Example with NativeFlow - lambda functions and 2 fields
    val player: MutableSharedFlow<Player>
        get() = repository.getPlayer()

    val playerNativeFlow: NativeFlow<Player>
        get() = player.asNativeFlow()


    // Example with Flow wrapper and custom Optional wrapper
    val playerOptional: MutableSharedFlow<Optional<Player>>
        get() = repository.getPlayerOptional()

    val playerFlowAdapter: FlowAdapter<Player>
        get() = FlowAdapter(scope, player)

    val playerOptionalFlowAdapter: FlowAdapter<Optional<Player>>
        get() = FlowAdapter(scope, playerOptional)
}


