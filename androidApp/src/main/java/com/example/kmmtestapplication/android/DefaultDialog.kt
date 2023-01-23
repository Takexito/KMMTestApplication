package com.example.kmmtestapplication.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.example.kmmtestapplication.sample.presentation.RootComponent

@Composable
fun DefaultDialog(
    rootComponent: RootComponent,
) {
    val dialogOverlay by rootComponent.dialogControl.dialogOverlay.subscribeAsState()

    dialogOverlay.overlay?.instance?.dialog?.also { dialog ->
        Dialog(onDismissRequest = { dialog.onDismiss() }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
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