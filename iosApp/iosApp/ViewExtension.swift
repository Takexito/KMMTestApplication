//
//  ViewExtension.swift
//  iosApp
//
//  Created by Andrey on 19.01.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

@available(iOS 15.0, *)
extension View {
    @ViewBuilder func alert<T, A>(
        item: T?,
        onDismiss: @escaping (T) -> Void,
        title: (T) -> Text,
        message: (T) -> Text,
        actions: (T) -> A
    ) -> some View where A : View {
        if let item = item {
                alert(
                    title(item),
                    isPresented: Binding(get: { true }, set: {_,_ in onDismiss(item) }),
                    actions: { actions(item) },
                    message: { message(item) }
                )
        } else {
            self
        }
    }
}
