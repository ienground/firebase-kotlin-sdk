package zone.ien.firebase.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

internal class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebasePush.eventSink.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val payloadData = message.data
        val notification = message.notification
        
        var targetTitle = notification?.title
        var targetBody = notification?.body
        
        val formatter = FirebasePush.notificationFormatter
        if (formatter != null) {
            val formatted = formatter.format(payloadData, targetTitle, targetBody)
            if (formatted != null) {
                targetTitle = formatted.title
                targetBody = formatted.body
            }
        }
        
        if (FirebasePush.displayMode == PushDisplayMode.AUTO_DISPLAY && (targetTitle != null || targetBody != null)) {
            showNotification(targetTitle ?: "", targetBody ?: "", payloadData)
        }
        
        val hasNotification = (targetTitle != null || targetBody != null)
        val hasPayload = payloadData.isNotEmpty()

        if (hasNotification && hasPayload) {
            FirebasePush.eventSink.onPushNotificationWithPayloadData(targetTitle, targetBody, payloadData)
        } else if (hasNotification) {
            FirebasePush.eventSink.onPushNotification(targetTitle, targetBody)
        } else if (hasPayload) {
            FirebasePush.eventSink.onPushPayloadData(payloadData)
        }
    }

    private fun showNotification(title: String, body: String, data: Map<String, String>) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, launchIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "fcm_high_importance_channel"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "FCM Notifications Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }
        val notiId = data["id"]?.toIntOrNull() ?: data["notiId"]?.toIntOrNull() ?: 0
        manager.notify(notiId, builder.build())
    }
}
