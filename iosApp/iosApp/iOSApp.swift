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
        test()
    }
    
    func test(){
        let playerCancel = vm.playerNativeFlow({ data in
            print(data)
        }, { err in
            print(err)
        })
    }
}

