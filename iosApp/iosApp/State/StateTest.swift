//
//  StateTest.swift
//  iosApp
//
//  Created by Andrey on 18.01.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class StateTest{
    
    let vm: ViewModel
    
    init(vm: ViewModel) {
        self.vm = vm
    }
    
    func testObservableState(){
        let state = ObservableState<KotlinInt>(vm.stateFlow)
        let subscription = state.$value.sink { value in
            print(value)
        }
    }
    
    func testNullableObservableState(){
        let state = NullableObservableState<KotlinInt>(vm.stateFlow)
        _ = state.$value.sink { value in
            print(value)
        }
    }
    
    func testNativeFlow(){
        print("=== Output with Native Flow ===")
        let cancel = vm.playerNativeFlow({ data in
            print(data)
        }, { err in
            print(err)
        })
    }

    func testWithWrappers(){
        print("=== Output with Wrappers ===")
            
        let playerCancel = vm.playerOptionalFlowAdapter.subscribe(onEach: { data in
            print(data.get())
        }, onComplete: {
            print("Complete")
        }, onThrow: { err in
            print(err)
        })
    }

    func testComputed(){
        let test = TestComputedFlow()
        let floatState = test.nativeFlow({ data in
            print(data)
        }, { err in
            print(err)
        })
    }
}
