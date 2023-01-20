package com.example.kmmtestapplication.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.defaultComponentContext
import com.example.kmmtestapplication.error.ErrorHandler
import com.example.kmmtestapplication.error.safeLaunch
import com.example.kmmtestapplication.message.data.MessageServiceImpl
import com.example.kmmtestapplication.sample.data.MainRepository
import com.example.kmmtestapplication.sample.data.PokemonWrapperResponse
import com.example.kmmtestapplication.sample.presentation.DefaultRootComponent
import com.example.kmmtestapplication.sample.presentation.RootComponent

class MainActivity : ComponentActivity() {

    private val messageService = MessageServiceImpl()
    private val handler = ErrorHandler(messageService)
    private val pokemonState = mutableStateOf<List<PokemonWrapperResponse>>(emptyList())
    private val mainRepository = MainRepository()
    private val rootComponent: RootComponent = DefaultRootComponent(defaultComponentContext())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestData()
        setContent()
    }

    private fun setContent() {
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Dialog(rootComponent)
                    BottomSheet(
                        messageService = messageService,
                        rootComponent = rootComponent,
                        pokemonState = pokemonState
                    )
                }
            }
        }
    }

    private fun requestData() {
        lifecycle.coroutineScope.safeLaunch(handler) {
            val list = mainRepository.getPokemonByType()
            pokemonState.value = list.pokemons
        }
    }
}
