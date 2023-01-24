import Foundation
import SwiftUI
import shared

@main
struct iOSApp: App {
    let vm = ViewModel()
    
    @ObservedObject
    private var state: ObservableState<KotlinInt>
    
    
	var body: some Scene {
		WindowGroup {
            Text("\(state.optionalValue ?? -1)")
		}
	}
    
    init(){
        state = ObservableState(vm.cStateFlow())
    }
}

