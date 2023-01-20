package com.example.kmmtestapplication.bottom_sheet

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.overlay.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface BottomSheetControl {
    val sheetOverlay: Value<ChildOverlay<*, BottomSheetComponent>>
    val sheetState: StateFlow<State>

    fun changeState(state: State)
    fun show(config: SheetConfig)
    fun dismiss(onComplete: (isSuccess: Boolean) -> Unit = {})

    enum class State {
        Expanded, Collapsed, Hidden
    }

    @Parcelize
    sealed class SheetConfig : Parcelable {
        data class DefaultBottomSheet(val id: String) : SheetConfig()
    }
}

class DefaultBottomSheetControl(
    componentContext: ComponentContext,
    key: String = SHEET_CHILD_OVERLAY_KEY
) : BottomSheetControl {

    companion object {
        private const val SHEET_CHILD_OVERLAY_KEY = "sheetChildOverlay"
    }

    private val _sheetState = MutableStateFlow(BottomSheetControl.State.Hidden)
    override val sheetState: StateFlow<BottomSheetControl.State>
        get() = _sheetState

    private val sheetNavigation = OverlayNavigation<BottomSheetControl.SheetConfig>()

    override val sheetOverlay: Value<ChildOverlay<*, BottomSheetComponent>> =
        componentContext.childOverlay(
            source = sheetNavigation,
            handleBackButton = true,
            key = key,
            childFactory = ::createSheetChild
        )

    override fun changeState(state: BottomSheetControl.State) {
        if (sheetOverlay.value.overlay?.instance == null) {
            println("BottomSheetControl: instance is null")
            return
        }
        _sheetState.tryEmit(state)
    }

    override fun show(config: BottomSheetControl.SheetConfig) {
        sheetNavigation.activate(config)
        _sheetState.tryEmit(BottomSheetControl.State.Expanded)
    }

    override fun dismiss(onComplete: (isSuccess: Boolean) -> Unit) {
        sheetNavigation.dismiss(onComplete)
        _sheetState.tryEmit(BottomSheetControl.State.Hidden)
    }

    private fun createSheetChild(
        config: BottomSheetControl.SheetConfig,
        componentContext: ComponentContext
    ): BottomSheetComponent = when (config) {
        is BottomSheetControl.SheetConfig.DefaultBottomSheet -> DefaultBottomSheetComponent(
            componentContext,
            ::dismiss
        )
    }
}