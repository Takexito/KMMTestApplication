package com.example.kmmtestapplication.sample.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.overlay.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.example.kmmtestapplication.dialogs.DefaultDialogComponent
import com.example.kmmtestapplication.dialogs.Dialog
import com.example.kmmtestapplication.dialogs.DialogComponent
import com.example.kmmtestapplication.property.scope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface RootComponent {
    val dialogChildOverlay: Value<ChildOverlay<*, DialogComponent>>
    fun onButtonClick()
}

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    companion object {
        private const val DIALOG_CHILD_OVERLAY_KEY = "dialogChildOverlay"
    }

    private val dialogNavigation = OverlayNavigation<DialogConfig>()

    override val dialogChildOverlay: Value<ChildOverlay<*, DialogComponent>> =
        componentContext.childOverlay(
            source = dialogNavigation,
            handleBackButton = true,
            key = DIALOG_CHILD_OVERLAY_KEY,
            childFactory = ::createDialogChild
        )

    private fun createDialogChild(
        config: DialogConfig,
        componentContext: ComponentContext
    ): DialogComponent = DefaultDialogComponent(componentContext, config.dialog)


    override fun onButtonClick() {
        showDialog()
    }

    private fun showDialog() {
        dialogNavigation.activate(
            DialogConfig(
                Dialog(
                    "Title",
                    "Message",
                    listOf(
                        Dialog.Button("Ok", ::okDialogButton),
                        Dialog.Button("Cancel", ::cancelDialogButton)
                    )
                ) { dialogNavigation.dismiss() })
        )
    }

    private fun okDialogButton() {
        dialogNavigation.dismiss {
            scope.launch {
                delay(1000)
                if (it) showDialog()
            }
        }
    }

    private fun cancelDialogButton() {
        dialogNavigation.dismiss()
    }

    @Parcelize
    private data class DialogConfig(val dialog: Dialog) : Parcelable
}

fun getRootComponent(): RootComponent {

    return DefaultRootComponent(
        DefaultComponentContext(LifecycleRegistry())
    )
}