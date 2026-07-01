import SwiftUI
import FirebaseCore
import FirebaseMessaging
import UserNotifications
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {

    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        
        // Configure native Firebase App
        if FirebaseApp.app() == nil {
            FirebaseApp.configure()
        }
        
        UNUserNotificationCenter.current().delegate = self
        
        // Request notifications authorization
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(options: authOptions) { granted, error in
            if let error = error {
                print("Notification authorization error: \(error.localizedDescription)")
            } else {
                print("Notification permission granted: \(granted)")
            }
        }
        
        // KMP KMPNotifier internally configures the FIRMessaging delegate and registers for remote notifications
        _ = KMPNotifier.shared.notifier
        
        return true
    }

    // Register APNs token inside Firebase Messaging client
    func application(_ application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }

    // Intercept background remote payloads and bridge to Kotlin KMPNotifier extension
    func application(_ application: UIApplication,
                     didReceiveRemoteNotification userInfo: [AnyHashable : Any],
                     fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        KMPNotifier.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
        completionHandler(.newData)
    }

    // Suppress native foreground banners and route payloads to Kotlin KMPNotifier extension
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        let userInfo = notification.request.content.userInfo
        KMPNotifier.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
        completionHandler([])
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}