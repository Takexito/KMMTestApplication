package com.example.kmmtestapplication.android

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arkivanov.decompose.router.overlay.ChildOverlay
import com.example.kmmtestapplication.bottom_sheet.BottomSheetComponent
import com.example.kmmtestapplication.bottom_sheet.DefaultBottomSheetComponent
import com.example.kmmtestapplication.sample.presentation.RootComponent
import kotlinx.coroutines.launch


@Composable
@OptIn(ExperimentalMaterialApi::class)
fun ModalBottomSheet(
    rootComponent: RootComponent,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetOverlay by rootComponent.bottomSheetControl.sheetOverlay.subscribeAsState()
    val modalBottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden, skipHalfExpanded = true)

    modalBottomSheetState.handleBottomSheetHiding(bottomSheetOverlay)

    ModalBottomSheetLayout(
        sheetContent = {
            when (bottomSheetOverlay.overlay?.instance) {
                is DefaultBottomSheetComponent -> {
                    Text(text = "Bottom Sheet!")
                    SideEffect {
                        coroutineScope.launch { modalBottomSheetState.show() }
                    }
                }
                null -> {
                    Box(Modifier.height(1.dp))
                    SideEffect {
                        coroutineScope.launch { modalBottomSheetState.hide() }
                    }
                }
            }
        },
        sheetState = modalBottomSheetState
    ) {
        content()
    }
}

@SuppressLint("ComposableNaming")
@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun ModalBottomSheetState.handleBottomSheetHiding(
    bottomSheetOverlay: ChildOverlay<*, BottomSheetComponent>
) {
    LaunchedEffect(currentValue) {
        if (currentValue == ModalBottomSheetValue.Hidden) {
            bottomSheetOverlay.overlay?.instance?.dismiss()
        }
    }
}