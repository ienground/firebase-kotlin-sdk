import SwiftUI
import FirebaseCore
import FirebaseMessaging
import FirebaseAppDistribution
import UserNotifications

class AppDelegate: NSObject, UIApplicationDelegate, MessagingDelegate {
    func application(
        _ app: UIApplication,
        open url: URL,
        options: [UIApplication.OpenURLOptionsKey : Any] = [:]
    ) -> Bool {
        let stringOptions = options.reduce(into: [String: Any]()) { result, element in
            result[element.key.rawValue] = element.value
        }
        if AppDistribution.appDistribution().application(app, open: url, options: stringOptions) {
            return true
        }
        return false
    }

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {
        // Configure Firebase Core API
        FirebaseApp.configure()
        
        // Connect FCM delegation handler
        Messaging.messaging().delegate = self
        
        // Request notification authorization on start
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
            print("FCM Debug: Notification permission status granted = \(granted)")
            if let error = error {
                print("FCM Debug: Request authorization failed = \(error.localizedDescription)")
            }
            guard granted else { return }
            
            // Register for remote notifications on the main thread
            DispatchQueue.main.async {
                UIApplication.shared.registerForRemoteNotifications()
                print("FCM Debug: registerForRemoteNotifications() invoked.")
            }
        }
        
        return true
    }

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        // Convert physical device token bytes to hex string for debugging
        let tokenString = deviceToken.map { String(format: "%02.2hhx", $0) }.joined()
        print("FCM Debug: successfully fetched physical APNs deviceToken = \(tokenString)")
        
        // Forward physical APNs device token to Firebase Messaging
        Messaging.messaging().apnsToken = deviceToken
    }

    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Error
    ) {
        print("FCM Debug: failed to register for remote notifications: \(error.localizedDescription)")
    }

    func application(
        _ application: UIApplication,
        didReceiveRemoteNotification userInfo: [AnyHashable : Any],
        fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void
    ) {
        // Forward remote notification payload to Firebase Messaging
        Messaging.messaging().appDidReceiveMessage(userInfo)
        completionHandler(.newData)
    }


    // MessagingDelegate method to receive registration tokens from FCM
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("FCM Debug: successfully received FCM registration token = \(fcmToken ?? "nil")")
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