package zone.ien.firebase.messaging

import platform.UserNotifications.UNNotificationContent
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter

// Centralized dynamic payload parser for Swift application didReceiveRemoteNotification
public fun FirebasePush.onApplicationDidReceiveRemoteNotification(userInfo: Map<Any?, *>) {
    val dataMap = mutableMapOf<String, String>()
    userInfo.forEach { (key, value) ->
        if (key is String && value is String) {
            dataMap[key] = value
        }
    }
    val filteredMap = dataMap.filter { !it.key.startsWith("gcm.") && it.key != "aps" }
    
    // Mix data payload using centralized KMP rules
    val formatted = formatNotification(userInfo)
    val originalNotifTitle = formatted?.title
    val originalNotifBody = formatted?.body

    // 1. Emit events to KMP listeners based on exclusive payload types
    val hasNotification = (originalNotifTitle != null || originalNotifBody != null)
    val hasPayload = filteredMap.isNotEmpty()

    if (hasNotification && hasPayload) {
        FirebasePush.eventSink.onPushNotificationWithPayloadData(originalNotifTitle, originalNotifBody, filteredMap)
    } else if (hasNotification) {
        FirebasePush.eventSink.onPushNotification(originalNotifTitle, originalNotifBody)
    } else if (hasPayload) {
        FirebasePush.eventSink.onPushPayloadData(filteredMap)
    }

    // 2. Local notification trigger
    if (FirebasePush.displayMode == PushDisplayMode.AUTO_DISPLAY && (originalNotifTitle != null || originalNotifBody != null)) {
        val content = UNMutableNotificationContent().apply {
            setTitle(originalNotifTitle ?: "")
            setBody(originalNotifBody ?: "")
            setSound(UNNotificationSound.defaultSound)
        }
        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(0.1, repeats = false)
        val notiId = filteredMap["id"]?.toIntOrNull() ?: filteredMap["notiId"]?.toIntOrNull() ?: 0
        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = notiId.toString(),
            content = content,
            trigger = trigger
        )
        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request) { _ -> }
    }
}

// Swift-facing dynamic layout formattings linked natively inside NotificationService extension
public fun FirebasePush.formatNotification(userInfo: Map<Any?, *>) : NotificationContent? {
    val dataMap = mutableMapOf<String, String>()
    userInfo.forEach { (key, value) ->
        if (key is String && value is String) {
            dataMap[key] = value
        }
    }
    val filteredData = dataMap.filter { !it.key.startsWith("gcm.") && it.key != "aps" }
    
    val aps = userInfo["aps"] as? Map<*, *>
    val alert = aps?.get("alert")
    
    var title: String? = null
    var body: String? = null
    
    if (alert is Map<*, *>) {
        title = alert["title"] as? String
        body = alert["body"] as? String
    } else if (alert is String) {
        body = alert
    }
    
    val formatter = FirebasePush.notificationFormatter
    if (formatter != null) {
        return formatter.format(filteredData, title, body)
    }
    
    if (title != null || body != null) {
        return NotificationContent(title, body)
    }
    
    return null
}
