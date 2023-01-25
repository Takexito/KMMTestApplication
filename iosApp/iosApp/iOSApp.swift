import Foundation
import SwiftUI
import shared

@main
struct iOSApp: App {
    let vm = ViewModel()
    
    @ObservedObject
    private var state: ObservableState<Optional<KotlinInt>>
    
    
	var body: some Scene {
		WindowGroup {
            let id = state.value
            Text("\(Int(id.getForce() ?? -1) + 5)")
		}
	}
    
    init(){
        state = ObservableState(vm.cStateFlow)
    }
}

