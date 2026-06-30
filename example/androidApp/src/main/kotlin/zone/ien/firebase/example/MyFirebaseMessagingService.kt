package zone.ien.firebase.example

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import zone.ien.firebase.messaging.FirebaseMessaging

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 1. If notification payload block is present, pop up system tray banner
        remoteMessage.notification?.let {
            sendNotification(it.title ?: "FCM Message", it.body ?: "")
        }

        // 2. If data payload keys exist, forward to KMP and trigger dynamic local notification
        if (remoteMessage.data.isNotEmpty()) {
            FirebaseMessaging.notifyMessageReceived(remoteMessage.data)
            
            // Dynamic client-side synthesis from data fields
            val nickname = remoteMessage.data["sender_nickname"]
            val content = remoteMessage.data["content"]
            if (nickname != null && content != null) {
                sendNotification("$nickname 님이 보낸 책 보고서", "내용: $content")
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Log or propagate token changes if required
    }

    private fun sendNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "fcm_default_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "FCM Notifications Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}
