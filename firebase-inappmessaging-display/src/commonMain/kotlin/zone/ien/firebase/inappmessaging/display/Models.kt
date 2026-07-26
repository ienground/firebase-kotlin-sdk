package zone.ien.firebase.inappmessaging.display

public data class InAppMessageMetadata(
    public val campaignId: String,
    public val campaignName: String,
    public val messageType: String
)

public data class InAppMessageCampaign(
    public val campaignId: String,
    public val campaignName: String,
    public val isTestMessage: Boolean
)

public enum class InAppMessageType {
    CARD,
    BANNER,
    MODAL,
    IMAGE_ONLY
}

public data class InAppMessageText(
    public val text: String?,
    public val color: String?
)

/**
 * 표시 메시지의 이미지 정보입니다.
 *
 * Android Firebase SDK는 원본 바이트 대신 URL과 디코딩된 Bitmap을 제공하므로 [rawData]는
 * Android에서 `null`입니다. 플랫폼별 재인코딩으로 원본과 다른 데이터를 만들지 않고 [imageUrl]을
 * 공통 이미지 식별자로 사용합니다.
 */
public data class InAppMessageImage(
    public val imageUrl: String,
    public val rawData: ByteArray? = null
)

public data class InAppMessageButton(
    public val text: InAppMessageText?,
    public val backgroundColor: String?
)

public data class InAppMessageAction(
    public val url: String? = null,
    public val button: InAppMessageButton? = null
)

public sealed interface InAppDisplayMessage {
    public val campaign: InAppMessageCampaign
    public val messageType: InAppMessageType
    public val title: InAppMessageText?
    public val body: InAppMessageText?
    public val image: InAppMessageImage?
    public val primaryAction: InAppMessageAction?
    public val secondaryAction: InAppMessageAction?
    public val backgroundColor: String?
    public val appData: Map<String, String>

    public data class Card(
        override val campaign: InAppMessageCampaign,
        override val title: InAppMessageText,
        override val body: InAppMessageText? = null,
        public val portraitImage: InAppMessageImage? = null,
        public val landscapeImage: InAppMessageImage? = null,
        override val primaryAction: InAppMessageAction,
        override val secondaryAction: InAppMessageAction? = null,
        override val backgroundColor: String? = null,
        override val appData: Map<String, String> = emptyMap()
    ) : InAppDisplayMessage {
        override val messageType: InAppMessageType = InAppMessageType.CARD
        override val image: InAppMessageImage?
            get() = portraitImage
    }

    public data class Banner(
        override val campaign: InAppMessageCampaign,
        override val title: InAppMessageText,
        override val body: InAppMessageText? = null,
        override val image: InAppMessageImage? = null,
        override val primaryAction: InAppMessageAction? = null,
        override val backgroundColor: String? = null,
        override val appData: Map<String, String> = emptyMap()
    ) : InAppDisplayMessage {
        override val messageType: InAppMessageType = InAppMessageType.BANNER
        override val secondaryAction: InAppMessageAction? = null
    }

    public data class Modal(
        override val campaign: InAppMessageCampaign,
        override val title: InAppMessageText,
        override val body: InAppMessageText? = null,
        override val image: InAppMessageImage? = null,
        override val primaryAction: InAppMessageAction? = null,
        override val backgroundColor: String? = null,
        override val appData: Map<String, String> = emptyMap()
    ) : InAppDisplayMessage {
        override val messageType: InAppMessageType = InAppMessageType.MODAL
        override val secondaryAction: InAppMessageAction? = null
    }

    public data class ImageOnly(
        override val campaign: InAppMessageCampaign,
        override val image: InAppMessageImage,
        override val primaryAction: InAppMessageAction? = null,
        override val appData: Map<String, String> = emptyMap()
    ) : InAppDisplayMessage {
        override val messageType: InAppMessageType = InAppMessageType.IMAGE_ONLY
        override val title: InAppMessageText? = null
        override val body: InAppMessageText? = null
        override val secondaryAction: InAppMessageAction? = null
        override val backgroundColor: String? = null
    }
}

public val InAppDisplayMessage.metadata: InAppMessageMetadata
    get() = InAppMessageMetadata(
        campaignId = campaign.campaignId,
        campaignName = campaign.campaignName,
        messageType = messageType.name
    )

internal fun InAppDisplayMessage.validatedAction(
    action: InAppMessageAction?
): InAppMessageAction? = action?.takeIf {
    it == primaryAction || it == secondaryAction
}

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
    public fun messageClicked() {}
    public fun messageClicked(action: InAppMessageAction?) {
        messageClicked()
    }
    public fun messageDismissed(dismissType: InAppMessageDismissType)
    public fun displayErrorEncountered(errorReason: InAppMessageErrorReason)
}

public interface InAppMessagingDisplayListener {
    public fun displayMessage(message: InAppMessageMetadata, callbacks: InAppMessagingDisplayCallbacks) {}

    public fun displayMessage(message: InAppDisplayMessage, callbacks: InAppMessagingDisplayCallbacks) {
        displayMessage(message.metadata, callbacks)
    }
}
