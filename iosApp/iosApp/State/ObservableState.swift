//
//  NullableObservableState.swift
//  iosApp
//
//  Created by Andrey on 18.01.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import shared


public class ObservableState<T: NSObject>: ObservableObject {
    
    private let observableState: FlowWrapper<T>
    
    @Published
    var value: T
    
    @Published
    var optionalValue: T? = nil
    
    private var cancelable: Cancelable? = nil
    
    
    init(_ state: CStateFlow<T>, _ defaultValue: T = NSObject()) {
        self.observableState = FlowWrapper<T>(flow: state)
        self.value = defaultValue
        if(state.type.isMarkedNullable) {
            optionalValue = state.value
        } else {
            self.value = state.value!
        }
        
         cancelable = observableState.bind(consumer: { value in
             if(state.type.isMarkedNullable) {
                 self.optionalValue = value
             } else {
                 self.value = value!
             }
         })
    }
    
        
    deinit {
        self.cancelable?.cancel()
    }
}
