//
//  ObservableValue.swift
//  iosApp
//
//  Created by Andrey on 19.01.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

public class ObservableValue<T : AnyObject> : ObservableObject {
    private let observableValue: DecomposeValue<T>

    @Published
    var value: T

    private var observer: ((T) -> Void)?
    
    init(_ value: DecomposeValue<T>) {
        observableValue = value
        self.value = observableValue.value
        observer = { [weak self] value in self?.value = value }
        observableValue.subscribe(observer: observer!)
    }

    deinit {
        observableValue.unsubscribe(observer: self.observer!)
    }
}
