package com.example.kmmtestapplication.android

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arkivanov.decompose.router.overlay.ChildOverlay
import com.example.kmmtestapplication.bottom_sheet.BottomSheetComponent
import com.example.kmmtestapplication.bottom_sheet.BottomSheetControl
import com.example.kmmtestapplication.bottom_sheet.DefaultBottomSheetComponent
import com.example.kmmtestapplication.sample.presentation.RootComponent
import kotlinx.coroutines.launch


@Composable
@OptIn(ExperimentalMaterialApi::class)
fun ModalBottomSheet(
    rootComponent: RootComponent,
    content: @Composable () -> Unit
) {
    val bottomSheetOverlay by rootComponent.bottomSheetControl.sheetOverlay.subscribeAsState()
    val modalBottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val componentBottomSheetState by rootComponent.bottomSheetControl.sheetState.collectAsState()

    modalBottomSheetState.handleBottomSheetState(bottomSheetOverlay, componentBottomSheetState)

    ModalBottomSheetLayout(
        sheetContent = {
            when (val child = bottomSheetOverlay.overlay?.instance) {
                is DefaultBottomSheetComponent -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Text(text = "Bottom Sheet!")
                        Button(onClick = {
                            child.expandBottomSheet()
                        }) {
                            Text(text = "Expand")
                        }
                        Button(onClick = {
                            child.expandBottomSheet()
                        }) {
                            Text(text = "Collapse")
                        }
                        Button(onClick = {
                            child.dismiss()
                        }) {
                            Text(text = "hide")
                        }
                    }
                }
                null -> {
                    Box(Modifier.height(1.dp))
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
private fun ModalBottomSheetState.handleBottomSheetState(
    bottomSheetOverlay: ChildOverlay<*, BottomSheetComponent>,
    componentBottomSheetState: BottomSheetControl.State
) {
    LaunchedEffect(componentBottomSheetState) {
        when (componentBottomSheetState) {
            BottomSheetControl.State.Expanded -> {
                launch { animateTo(ModalBottomSheetValue.Expanded) }
            }
            BottomSheetControl.State.Collapsed -> {
                launch { animateTo(ModalBottomSheetValue.HalfExpanded) }
            }
            BottomSheetControl.State.Hidden -> {
                launch { animateTo(ModalBottomSheetValue.Hidden) }
            }
        }
    }

    LaunchedEffect(currentValue) {
        if (currentValue == ModalBottomSheetValue.Hidden) {
            bottomSheetOverlay.overlay?.instance?.dismiss()
        }
    }
}