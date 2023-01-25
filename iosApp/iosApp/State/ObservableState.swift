//
//  NullableObservableState.swift
//  iosApp
//
//  Created by Andrey on 18.01.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import shared


public class ObservableState<T: AnyObject>: ObservableObject {
    
    private let observableState: FlowWrapper<T>
    
    @Published
    var value: T
    
    
    private var cancelable: Cancelable? = nil
    
    
    init(_ state: CStateFlow<T>) {
        self.observableState = FlowWrapper<T>(flow: state)
        self.value = state.value
        
         cancelable = observableState.bind(consumer: { value in
             self.value = value
         })
    }
    
        
    deinit {
        self.cancelable?.cancel()
    }
}
