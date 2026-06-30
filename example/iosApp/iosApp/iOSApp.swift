import SwiftUI
import FirebaseCore
import FirebaseMessaging
import UserNotifications
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        
        // Guard configuration duplication with KMP common initializers
        if FirebaseApp.app() == nil {
            FirebaseApp.configure()
        }
        
        UNUserNotificationCenter.current().delegate = self
        Messaging.messaging().delegate = self
        
        // Request notification permission dynamically
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(options: authOptions) { granted, error in
            if let error = error {
                print("Notification authorization error: \(error.localizedDescription)")
            } else {
                print("Notification permission granted: \(granted)")
            }
        }
        
        application.registerForRemoteNotifications()
        
        return true
    }

    // Register APNs token inside Firebase Messaging client
    func application(_ application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }

    // Monitor token refresh callback events
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("FCM registration token: \(String(describing: fcmToken))")
    }

    // Intercept background remote payloads
    func application(_ application: UIApplication,
                     didReceiveRemoteNotification userInfo: [AnyHashable : Any],
                     fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        forwardPayloadToKMP(userInfo)
        triggerLocalNotification(from: userInfo)
        completionHandler(.newData)
    }

    // Present notification banner even when application is in foreground
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        let userInfo = notification.request.content.userInfo
        forwardPayloadToKMP(userInfo)
        triggerLocalNotification(from: userInfo)
        
        // Retain and trigger native system banner rendering for notification payload blocks
        completionHandler([[.banner, .sound, .badge]])
    }

    private func forwardPayloadToKMP(_ userInfo: [AnyHashable: Any]) {
        var dataMap: [String: String] = [:]
        for (key, value) in userInfo {
            if let stringKey = key as? String, let stringValue = value as? String {
                dataMap[stringKey] = stringValue
            }
        }
        
        // Skip system metadata keys (e.g. aps, gcm.message_id) before KMP notification
        let filteredMap = dataMap.filter { !$0.key.hasPrefix("gcm.") && $0.key != "aps" }
        if !filteredMap.isEmpty {
            ComposeApp.FirebaseMessaging.companion.notifyMessageReceived(data: filteredMap)
        }
    }

    private func triggerLocalNotification(from userInfo: [AnyHashable: Any]) {
        // Parse custom data fields on iOS client side
        guard let nickname = userInfo["sender_nickname"] as? String,
              let content = userInfo["content"] as? String else {
            return
        }

        let notificationContent = UNMutableNotificationContent()
        // Dynamically synthesize title and body locally on iOS app
        notificationContent.title = "\(nickname) 님이 보낸 책 보고서"
        notificationContent.body = "내용: \(content)"
        notificationContent.sound = .default

        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 0.1, repeats: false)
        let request = UNNotificationRequest(identifier: UUID().uuidString, content: notificationContent, trigger: trigger)

        UNUserNotificationCenter.current().add(request) { error in
            if let error = error {
                print("Error displaying local notification: \(error.localizedDescription)")
            }
        }
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