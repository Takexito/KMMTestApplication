package com.example.kmmtestapplication.dialog

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.overlay.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

interface DialogControl {
    val dialogOverlay: Value<ChildOverlay<*, DialogComponent>>

    fun show(config: DialogConfig)
    fun dismiss(onComplete: (isSuccess: Boolean) -> Unit = {})

    @Parcelize
    sealed class DialogConfig : Parcelable {
        data class DefaultDialog(val dialog: Dialog) : DialogConfig()
    }
}

class DefaultDialogControl(componentContext: ComponentContext, key: String = DIALOG_CHILD_OVERLAY_KEY) : DialogControl {

    companion object {
        private const val DIALOG_CHILD_OVERLAY_KEY = "dialogChildOverlay"
    }

    private val dialogNavigation = OverlayNavigation<DialogControl.DialogConfig>()

    override val dialogOverlay: Value<ChildOverlay<*, DialogComponent>> =
        componentContext.childOverlay(
            source = dialogNavigation,
            handleBackButton = true,
            key = key,
            childFactory = ::createDialogChild
        )

    override fun show(config: DialogControl.DialogConfig) {
        dialogNavigation.activate(config)
    }

    override fun dismiss(onComplete: (isSuccess: Boolean) -> Unit) {
        dialogNavigation.dismiss(onComplete)
    }

    private fun createDialogChild(
        config: DialogControl.DialogConfig,
        componentContext: ComponentContext
    ): DialogComponent = when (config) {
        is DialogControl.DialogConfig.DefaultDialog -> DefaultDialogComponent(
            componentContext,
            config.dialog
        )
    }
}