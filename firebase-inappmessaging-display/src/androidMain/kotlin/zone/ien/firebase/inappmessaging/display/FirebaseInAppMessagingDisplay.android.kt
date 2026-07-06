package zone.ien.firebase.inappmessaging.display

import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplay as AndroidDisplay

public actual class FirebaseInAppMessagingDisplay internal actual constructor() {

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

            val wrappedCallbacks = object : InAppMessagingDisplayCallbacks {
                override fun impressionDetected() {
                    callbacks.impressionDetected()
                }

                override fun messageClicked() {
                    val action = when (inAppMessage) {
                        is com.google.firebase.inappmessaging.model.CardMessage -> inAppMessage.primaryAction
                        is com.google.firebase.inappmessaging.model.BannerMessage -> inAppMessage.action
                        is com.google.firebase.inappmessaging.model.ModalMessage -> inAppMessage.action
                        is com.google.firebase.inappmessaging.model.ImageOnlyMessage -> inAppMessage.action
                        else -> null
                    }
                    if (action != null) {
                        callbacks.messageClicked(action)
                    }
                }

                override fun messageDismissed(dismissType: InAppMessageDismissType) {
                    val androidDismissType = when (dismissType) {
                        InAppMessageDismissType.CLICK -> com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks.InAppMessagingDismissType.CLICK
                        InAppMessageDismissType.SWIPE -> com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks.InAppMessagingDismissType.SWIPE
                        InAppMessageDismissType.AUTO -> com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks.InAppMessagingDismissType.AUTO
                        InAppMessageDismissType.UNKNOWN -> com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks.InAppMessagingDismissType.UNKNOWN_DISMISS_TYPE
                    }
                    callbacks.messageDismissed(androidDismissType)
                }

                override fun displayErrorEncountered(errorReason: InAppMessageErrorReason) {
                    val androidErrorReason = when (errorReason) {
                        InAppMessageErrorReason.UNSPECIFIED_RENDER_ERROR -> com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks.InAppMessagingErrorReason.UNSPECIFIED_RENDER_ERROR
                        InAppMessageErrorReason.IMAGE_FETCH_ERROR -> com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks.InAppMessagingErrorReason.IMAGE_FETCH_ERROR
                        InAppMessageErrorReason.IMAGE_DISPLAY_ERROR -> com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks.InAppMessagingErrorReason.IMAGE_DISPLAY_ERROR
                        InAppMessageErrorReason.IMAGE_UNSUPPORTED_FORMAT -> com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks.InAppMessagingErrorReason.IMAGE_UNSUPPORTED_FORMAT
                    }
                    callbacks.displayErrorEncountered(androidErrorReason)
                }
            }

            listener.displayMessage(metadata, wrappedCallbacks)
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
