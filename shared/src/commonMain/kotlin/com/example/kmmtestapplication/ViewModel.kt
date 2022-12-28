package com.example.kmmtestapplication

import kotlinx.coroutines.*
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
    val data: MutableStateFlow<Float>
        get() = repository.getData()

    val player: MutableSharedFlow<Player>
        get() = repository.getPlayer()

    val nativeFlow: NativeFlow<Float>
        get() = data.asNativeFlow()

    val playerNativeFlow: NativeFlow<Player>
        get() = player.asNativeFlow()


    val cFlow: CFlow<Float>
        get() = data.wrap()

    fun returnNativeFlow(): NativeFlow<Float> {
        return data.asNativeFlow()
    }
}


fun interface Closeable {
    fun close()
}

class CFlow<T : Any> internal constructor(private val origin: Flow<T>) : Flow<T> by origin {
    fun watch(block: (T) -> Unit): Closeable {
        val job = Job()

        onEach {
            block(it)
        }.launchIn(CoroutineScope(Dispatchers.Main + job))

        return Closeable { job.cancel() }
    }
}

internal fun <T : Any> Flow<T>.wrap(): CFlow<T> = CFlow(this)


