//
//  BottomSheetView.swift
//  iosApp
//
//  Created by Andrey on 19.01.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

@available(iOS 15.0, *)
struct BottomSheetView: View{
    
    init(rootComponent: RootComponent){
        self.rootComponent = rootComponent
        sheetOverlay = ObservableValue(rootComponent.bottomSheetControl.sheetOverlay)
    }
    
    private var rootComponent: RootComponent
    
    @ObservedObject
    private var sheetOverlay: ObservableValue<DecomposeChildOverlay<AnyObject, BottomSheetComponent>>
    
    var body: some View{
        VStack(alignment: .center, spacing: 8) {
            Button("Show Bottom Sheet", action: rootComponent.onButtonClick)
        }
        .sheet(isPresented:
                Binding(
                    get: { sheetOverlay.value.overlay?.instance != nil },
                    set: {_,_ in
                        rootComponent.bottomSheetControl.dismiss(onComplete: {_ in})
                    }
                )
        ) {
            VStack(alignment: .center, spacing: 8) {
                Text("Show Dialog")
            }
        }
    }
}
