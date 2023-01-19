//
//  NullableObservableState.swift
//  iosApp
//
//  Created by Andrey on 18.01.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import shared


public class NullableObservableState<T: AnyObject> : ObservableObject {
    
    private let observableState: FlowWrapper<T>
    
    @Published
    var value: T?
    
    private var cancelable: Cancelable? = nil
    
    init(_ value: Kotlinx_coroutines_coreStateFlow) {
        self.observableState = FlowWrapper<T>(flow: value)
         self.value = value.value as? T
         cancelable = observableState.bind(consumer: { value in
             self.value = value
         })
    }
    
    init(initialValue: T, flow: Kotlinx_coroutines_coreFlow) {
        self.observableState = FlowWrapper<T>(flow: flow)
        self.value = initialValue
        cancelable = observableState.bind(consumer: { value in
            self.value = value
        })
    }
        
    deinit {
        self.cancelable?.cancel()
    }
}

extension Kotlinx_coroutines_coreStateFlow{
    func asNullableObservableState<T : AnyObject>(_ type: T.Type) -> NullableObservableState<T>{
        return NullableObservableState(self)
    }
}
