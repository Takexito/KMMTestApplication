package com.example.kmmtestapplication.android

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.kmmtestapplication.message.data.MessageService
import com.example.kmmtestapplication.sample.data.PokemonWrapperResponse
import com.example.kmmtestapplication.sample.presentation.RootComponent

@Composable
private fun Content(
    messageService: MessageService,
    rootComponent: RootComponent,
    pokemonState: MutableState<List<PokemonWrapperResponse>>
) {
    val messageState = messageService.messageFlow.collectAsState(initial = null)
    Column(Modifier.fillMaxSize()) {
        Text(messageState.value.toString())
        Button(onClick = rootComponent::onButtonClick) {
            Text("Show Dialog")
        }
        pokemonState.value.forEach {
            Text(it.pokemon.name)
        }
    }
}

@Composable
fun BottomSheet(
    messageService: MessageService,
    rootComponent: RootComponent,
    pokemonState: MutableState<List<PokemonWrapperResponse>>
) {
//    ScaffoldBottomSheet(rootComponent) {
//        Content(messageService, rootComponent, pokemonState)
//    }
    ModalBottomSheet(rootComponent) {
        Content(messageService, rootComponent, pokemonState)
    }
}
