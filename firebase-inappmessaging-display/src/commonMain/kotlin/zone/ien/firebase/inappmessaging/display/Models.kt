package zone.ien.firebase.inappmessaging.display

public class InAppMessageMetadata(
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

public interface InAppMessagingDisplayListener {
    public fun onMessageDisplayed(message: InAppMessageMetadata)
    public fun onMessageClicked(message: InAppMessageMetadata)
    public fun onMessageDismissed(message: InAppMessageMetadata, dismissType: InAppMessageDismissType)
    public fun onMessageError(message: InAppMessageMetadata, exception: Exception)
}
