//
//  Dialog.swift
//  iosApp
//
//  Created by Andrey on 19.01.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

@available(iOS 15.0, *)
struct DialogView: View{
    
    init(rootComponent: RootComponent){
        self.rootComponent = rootComponent
        dialogOverlay = ObservableValue(rootComponent.dialogControl.dialogOverlay)
    }
    
    private var rootComponent: RootComponent
    
    @ObservedObject
    private var dialogOverlay: ObservableValue<DecomposeChildOverlay<AnyObject, DialogComponent>>
    
    var body: some View{
        VStack(alignment: .center, spacing: 8) {
            Button("Show Dialog", action: rootComponent.onButtonClick)
        }
        .alert(
            item: dialogOverlay.value.overlay?.instance,
            onDismiss: { $0.dialog.onDismiss() },
            title: { Text($0.dialog.title) },
            message: { Text($0.dialog.message) },
            actions: {
                ForEach($0.dialog.buttons, id: \.title) { button in
                    Button(button.title, action: { button.action() })
                }
            }
        )
    }
}
