package com.example.kmmtestapplication.dialog

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
data class Dialog(
    val title: String,
    val message: String,
    val buttons: List<Button>,
    val onDismiss: () -> Unit
) : Parcelable {
    @Parcelize
    data class Button(val title: String, val action: () -> Unit) : Parcelable
}

interface DialogComponent {
    val dialog: Dialog
}

class DefaultDialogComponent(
    private val componentContext: ComponentContext,
    override val dialog: Dialog
) : DialogComponent, ComponentContext by componentContext