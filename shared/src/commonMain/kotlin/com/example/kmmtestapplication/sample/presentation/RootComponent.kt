package com.example.kmmtestapplication.sample.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.example.kmmtestapplication.bottom_sheet.BottomSheetControl
import com.example.kmmtestapplication.bottom_sheet.DefaultBottomSheetControl
import com.example.kmmtestapplication.dialog.DefaultDialogControl
import com.example.kmmtestapplication.dialog.Dialog
import com.example.kmmtestapplication.dialog.DialogControl
import com.example.kmmtestapplication.property.scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface RootComponent {
    val dialogControl: DialogControl
    val bottomSheetControl: BottomSheetControl
    fun onButtonClick()
}

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    companion object {
        private const val DIALOG_CHILD_OVERLAY_KEY = "dialogOverlay"
        private const val SHEET_CHILD_OVERLAY_KEY = "sheetOverlay"
    }

    override val dialogControl = DefaultDialogControl(componentContext, DIALOG_CHILD_OVERLAY_KEY)
    override val bottomSheetControl =
        DefaultBottomSheetControl(componentContext, SHEET_CHILD_OVERLAY_KEY)

    override fun onButtonClick() {
//        showDialog()
        showBottomSheet()
    }

    private fun showBottomSheet() {
        bottomSheetControl.show(BottomSheetControl.SheetConfig.DefaultBottomSheet("id"))
    }

    private fun showDialog() {
        dialogControl.show(
            DialogControl.DialogConfig.DefaultDialog(
                Dialog(
                    "Title",
                    "Message",
                    listOf(
                        Dialog.Button("Ok", ::okDialogButton),
                        Dialog.Button("Cancel", ::cancelDialogButton)
                    )
                ) { dialogControl.dismiss() })
        )
    }

    private fun okDialogButton() {
        dialogControl.dismiss {
            scope.launch(Dispatchers.Main) {
                delay(1000)
                if (it) showDialog()
            }
        }
    }

    private fun cancelDialogButton() {
        dialogControl.dismiss()
    }
}

fun getRootComponent(): RootComponent {

    return DefaultRootComponent(
        DefaultComponentContext(LifecycleRegistry())
    )
}