package zone.ien.firebase.inappmessaging.display

import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplay as AndroidDisplay
import com.google.firebase.inappmessaging.model.InAppMessage

public actual class FirebaseInAppMessagingDisplay internal constructor() {

    public actual fun setCustomDisplayListener(listener: InAppMessagingDisplayListener) {
        val androidDisplay = AndroidDisplay { inAppMessage, callbacks ->
            val campaignMetadata = inAppMessage.campaignMetadata
            val campaignId = campaignMetadata?.campaignId ?: ""
            val campaignName = campaignMetadata?.campaignName ?: ""
            val messageType = inAppMessage.messageType?.name ?: ""

            val metadata = InAppMessageMetadata(
                campaignId = campaignId,
                campaignName = campaignName,
                messageType = messageType
            )

            // Trigger Display Callback
            listener.onMessageDisplayed(metadata)

            // Automatically report impression to ensure Google Analytics campaign statistics match
            callbacks.impressionDetected()
            
            // To simulate clicks/dismissals for verification:
            // For simple headless verification, impression tracking is sufficient.
        }

        com.google.firebase.inappmessaging.FirebaseInAppMessaging.getInstance().setMessageDisplayComponent(androidDisplay)
    }

    public actual fun clearCustomDisplayListener() {
        com.google.firebase.inappmessaging.FirebaseInAppMessaging.getInstance().clearDisplayListener()
    }

    public actual companion object {
        public actual val instance: FirebaseInAppMessagingDisplay = FirebaseInAppMessagingDisplay()
    }
}
