import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        KoinInitializerKt.InitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}