import UserNotifications
import ComposeApp

class NotificationService: UNNotificationServiceExtension {

    var contentHandler: ((UNNotificationContent) -> Void)?
    var bestAttemptContent: UNMutableNotificationContent?

    override func didReceive(_ request: UNNotificationRequest, withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void) {
        self.contentHandler = contentHandler
        bestAttemptContent = (request.content.mutableCopy() as? UNMutableNotificationContent)
        
        // Initialize centralized push configuration inside App Extension process
        KmpPushInitializer.shared.initialize(formatter: ComposeApp.ExampleNotificationFormatter())
        
        if let bestAttemptContent = bestAttemptContent {
            let userInfo = bestAttemptContent.userInfo
            
            // Centralized formatting rule call from KMP framework
            if let formatted = FirebasePush.shared.formatNotification(userInfo: userInfo) {
                bestAttemptContent.title = formatted.title ?? ""
                bestAttemptContent.body = formatted.body ?? ""
            }
            
            contentHandler(bestAttemptContent)
        }
    }
    
    override func serviceExtensionTimeWillExpire() {
        if let contentHandler = contentHandler, let bestAttemptContent = bestAttemptContent {
            contentHandler(bestAttemptContent)
        }
    }
}
