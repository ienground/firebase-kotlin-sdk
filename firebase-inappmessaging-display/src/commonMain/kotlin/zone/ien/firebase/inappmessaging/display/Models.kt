package zone.ien.firebase.inappmessaging.display

public data class InAppMessageMetadata(
    public val campaignId: String,
    public val campaignName: String,
    public val messageType: String
)

public enum class InAppMessageDismissType {
    CLICK,
    SWIPE,
    AUTO,
    UNKNOWN
}

public enum class InAppMessageErrorReason {
    UNSPECIFIED_RENDER_ERROR,
    IMAGE_FETCH_ERROR,
    IMAGE_DISPLAY_ERROR,
    IMAGE_UNSUPPORTED_FORMAT
}

public interface InAppMessagingDisplayCallbacks {
    public fun impressionDetected()
    public fun messageClicked()
    public fun messageDismissed(dismissType: InAppMessageDismissType)
    public fun displayErrorEncountered(errorReason: InAppMessageErrorReason)
}

public interface InAppMessagingDisplayListener {
    public fun displayMessage(message: InAppMessageMetadata, callbacks: InAppMessagingDisplayCallbacks)
}