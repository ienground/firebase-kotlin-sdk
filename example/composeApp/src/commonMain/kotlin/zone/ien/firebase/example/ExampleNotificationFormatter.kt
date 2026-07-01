package zone.ien.firebase.example

import zone.ien.firebase.messaging.NotificationContent
import zone.ien.firebase.messaging.NotificationFormatter

public class ExampleNotificationFormatter : NotificationFormatter {
    override fun format(data: Map<String, String>, title: String?, body: String?): NotificationContent? {
        val nickname = data["sender_nickname"]
        val content = data["content"]
        if (nickname != null && content != null) {
            return NotificationContent(
                title = "$nickname 님이 보낸 책 보고서? $title",
                body = "내용: $nickname $content"
            )
        }
        if (title != null || body != null) {
            return NotificationContent(title, body)
        }
        return null
    }
}
