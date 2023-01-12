package com.example.kmmtestapplication.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.coroutineScope
import com.example.kmmtestapplication.error.ErrorHandler
import com.example.kmmtestapplication.greeting.Greeting
import com.example.kmmtestapplication.message.data.MessageServiceImpl
import com.example.kmmtestapplication.sample.data.MainRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.onEach

class MainActivity : ComponentActivity() {

    val messageService = MessageServiceImpl()
    val handler = ErrorHandler(messageService)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        messageService.messageFlow.onEach {  }
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val state = messageService.messageFlow.collectAsState(initial = null)
                    GreetingView(state.value.toString())
                }
            }
        }
        val mainRepository = MainRepository()
        lifecycle.coroutineScope.launchWhenCreated {
            try {
                val list = mainRepository.getPokemonByType()
                list.pokemons.forEach {
                    println(it.pokemon.name)
                }
            } catch (e: Exception){
                handler.handleError(e)
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
