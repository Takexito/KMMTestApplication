import SwiftUI
import shared

@main
struct iOSApp: App {
    let vm = ViewModel(repository: Repository())
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
    
    init(){
//        testWithWrappers()
        testNativeFlow()
//        testComputed()
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

