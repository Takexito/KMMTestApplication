package com.example.kmmtestapplication.android

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arkivanov.decompose.router.overlay.ChildOverlay
import com.example.kmmtestapplication.bottom_sheet.BottomSheetComponent
import com.example.kmmtestapplication.bottom_sheet.DefaultBottomSheetComponent
import com.example.kmmtestapplication.sample.data.PokemonWrapperResponse
import com.example.kmmtestapplication.sample.presentation.RootComponent
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun ScaffoldBottomSheet(
    rootComponent: RootComponent,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val state: BottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))
    val bottomSheetOverlay by rootComponent.bottomSheetControl.sheetOverlay.subscribeAsState()

    state.bottomSheetState.handleBottomSheetHiding(bottomSheetOverlay = bottomSheetOverlay)

    BottomSheetScaffold(
        sheetPeekHeight = 0.dp,
        sheetContent = {
            when (bottomSheetOverlay.overlay?.instance) {
                is DefaultBottomSheetComponent -> {
                    Box(
                        Modifier
                            .height(350.dp)
                            .fillMaxWidth()
                            .background(Color.Cyan)
                    ) {
                        Text(text = "Bottom Sheet!")
                    }
                    SideEffect {
                        coroutineScope.launch { state.bottomSheetState.expand() }
                    }
                }
                null -> {
                    Box(Modifier.height(1.dp))
                    SideEffect {
                        coroutineScope.launch { state.bottomSheetState.collapse() }
                    }
                }
            }
        },
        scaffoldState = state
    ) {
        content()
    }
}


@SuppressLint("ComposableNaming")
@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun BottomSheetState.handleBottomSheetHiding(
    bottomSheetOverlay: ChildOverlay<*, BottomSheetComponent>
) {
    LaunchedEffect(currentValue) {
        if (currentValue == BottomSheetValue.Collapsed) {
            bottomSheetOverlay.overlay?.instance?.dismiss()
        }
    }

}