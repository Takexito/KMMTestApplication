package com.example.kmmtestapplication.bottom_sheet

import com.arkivanov.decompose.ComponentContext

interface BottomSheetComponent {
    fun dismiss(onComplete: (isSuccess: Boolean) -> Unit = {})
}

class DefaultBottomSheetComponent(
    private val componentContext: ComponentContext,
    private val onDismiss: (onComplete: (isSuccess: Boolean) -> Unit) -> Unit
) : BottomSheetComponent, ComponentContext by componentContext {
    override fun dismiss(onComplete: (isSuccess: Boolean) -> Unit) {
        onDismiss(onComplete)
    }
}