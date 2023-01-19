package com.example.kmmtestapplication.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.example.kmmtestapplication.error.ErrorHandler
import com.example.kmmtestapplication.error.safeLaunch
import com.example.kmmtestapplication.message.data.MessageServiceImpl
import com.example.kmmtestapplication.sample.data.*
import com.example.kmmtestapplication.sample.presentation.DefaultRootComponent
import com.example.kmmtestapplication.sample.presentation.RootComponent

class MainActivity : ComponentActivity() {

    val messageService = MessageServiceImpl()
    val handler = ErrorHandler(messageService)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pokemonState = mutableStateOf<List<PokemonWrapperResponse>>(emptyList())
        val mainRepository = MainRepository()
        val rootComponent: RootComponent = DefaultRootComponent(defaultComponentContext())
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
                    Dialog(rootComponent)

                    val messageState = messageService.messageFlow.collectAsState(initial = null)
                    Column(Modifier.fillMaxSize()) {
                        GreetingView(messageState.value.toString())
                        Button(onClick = rootComponent::onButtonClick) {
                            Text("Show Dialog")
                        }
                        pokemonState.value.forEach {
                            GreetingView(it.pokemon.name)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Dialog(
        rootComponent: RootComponent,
    ) {
        val dialogOverlay by rootComponent.dialogChildOverlay.subscribeAsState()

        dialogOverlay.overlay?.instance?.dialog?.also { dialog ->
            Dialog(onDismissRequest = { dialog.onDismiss() }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround) {
                    Text(text = dialog.title)
                    Text(text = dialog.message)
                    Row {
                        dialog.buttons.forEach { button ->
                            Button(onClick = { button.action() }) {
                                Text(text = button.title)
                            }
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
