package zone.ien.firebase.example

import zone.ien.firebase.messaging.NotificationContent
import zone.ien.firebase.messaging.NotificationFormatter

public class ExampleNotificationFormatter : NotificationFormatter {
    override fun format(data: zone.ien.firebase.messaging.PayloadData, title: String?, body: String?): NotificationContent? {
        // Dynamic placeholder interpolation: replaces {{key}} with data[key]
        fun String.interpolate(): String {
            var result = this
            data.forEach { (key, value) ->
                val strValue = value?.toString() ?: ""
                result = result.replace("{{$key}}", strValue)
            }
            return result
        }

        val finalTitle = title?.interpolate()
        val finalBody = body?.interpolate()

        if (finalTitle != null || finalBody != null) {
            return NotificationContent(finalTitle, finalBody)
        }
        return null
    }
}
