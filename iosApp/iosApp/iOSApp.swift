import SwiftUI
import shared

@available(iOS 15.0, *)
@main
struct iOSApp: App {
    
    let vm = ViewModel(repository: Repository())
    let rootComponent: RootComponent = RootComponentKt.getRootComponent()
    
    var body: some Scene {
        WindowGroup{
            VStack {
                DialogView(rootComponent: rootComponent)
            }
        }
    }
}
