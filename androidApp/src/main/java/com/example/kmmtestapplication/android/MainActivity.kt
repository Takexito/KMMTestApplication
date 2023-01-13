package com.example.kmmtestapplication.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.coroutineScope
import com.example.kmmtestapplication.error.ErrorHandler
import com.example.kmmtestapplication.error.safeLaunch
import com.example.kmmtestapplication.message.data.MessageServiceImpl
import com.example.kmmtestapplication.sample.data.MainRepository
import com.example.kmmtestapplication.sample.data.PokemonWrapperResponse

class MainActivity : ComponentActivity() {

    val messageService = MessageServiceImpl()
    val handler = ErrorHandler(messageService)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pokemonState = mutableStateOf<List<PokemonWrapperResponse>>(emptyList())
        val mainRepository = MainRepository()
        lifecycle.coroutineScope.safeLaunch(handler) {
            val list = mainRepository.getPokemonByType()
            pokemonState.value = list.pokemons
        }

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val messageState = messageService.messageFlow.collectAsState(initial = null)
                    Column(Modifier.fillMaxSize()) {
                        GreetingView(messageState.value.toString())
                        pokemonState.value.forEach {
                            GreetingView(it.pokemon.name)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
