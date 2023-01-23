package com.example.kmmtestapplication.bottom_sheet

import com.arkivanov.decompose.ComponentContext

interface BottomSheetComponent {
    fun dismiss(onComplete: (isSuccess: Boolean) -> Unit = {})
}

class DefaultBottomSheetComponent(
    private val componentContext: ComponentContext,
    private val bottomSheetControl: BottomSheetControl
) : BottomSheetComponent, ComponentContext by componentContext {
    override fun dismiss(onComplete: (isSuccess: Boolean) -> Unit) {
        bottomSheetControl.dismiss(onComplete)
    }

    fun expandBottomSheet(){
        bottomSheetControl.changeState(BottomSheetControl.State.Expanded)
    }

    fun collapseBottomSheet() {
        bottomSheetControl.changeState(BottomSheetControl.State.Collapsed)
    }
}