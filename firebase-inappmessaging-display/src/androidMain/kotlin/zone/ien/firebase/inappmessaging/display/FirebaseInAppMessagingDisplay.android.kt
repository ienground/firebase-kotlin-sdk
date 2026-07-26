package zone.ien.firebase.inappmessaging.display

import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks as AndroidCallbacks
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplay as AndroidDisplay
import com.google.firebase.inappmessaging.model.Action as AndroidAction
import com.google.firebase.inappmessaging.model.BannerMessage as AndroidBannerMessage
import com.google.firebase.inappmessaging.model.Button as AndroidButton
import com.google.firebase.inappmessaging.model.CardMessage as AndroidCardMessage
import com.google.firebase.inappmessaging.model.ImageData as AndroidImageData
import com.google.firebase.inappmessaging.model.ImageOnlyMessage as AndroidImageOnlyMessage
import com.google.firebase.inappmessaging.model.InAppMessage as AndroidInAppMessage
import com.google.firebase.inappmessaging.model.ModalMessage as AndroidModalMessage
import com.google.firebase.inappmessaging.model.Text as AndroidText

public actual class FirebaseInAppMessagingDisplay internal actual constructor() {

    public actual fun setCustomDisplayListener(listener: InAppMessagingDisplayListener) {
        val androidDisplay = AndroidDisplay { inAppMessage, callbacks ->
            val message = inAppMessage.toInAppDisplayMessage()
            listener.displayMessage(
                message,
                AndroidDisplayCallbacks(message, inAppMessage, callbacks)
            )
        }

        FirebaseInAppMessaging.getInstance().setMessageDisplayComponent(androidDisplay)
    }

    public actual fun clearCustomDisplayListener() {
        FirebaseInAppMessaging.getInstance().clearDisplayListener()
    }

    public actual companion object {
        public actual val instance: FirebaseInAppMessagingDisplay = FirebaseInAppMessagingDisplay()
    }
}

private class AndroidDisplayCallbacks(
    private val message: InAppDisplayMessage,
    private val androidMessage: AndroidInAppMessage,
    private val callbacks: AndroidCallbacks
) : InAppMessagingDisplayCallbacks {
    override fun impressionDetected() {
        callbacks.impressionDetected()
    }

    override fun messageClicked() {
        messageClicked(message.primaryAction)
    }

    override fun messageClicked(action: InAppMessageAction?) {
        androidMessage.androidActionFor(message.validatedAction(action))?.let(callbacks::messageClicked)
    }

    override fun messageDismissed(dismissType: InAppMessageDismissType) {
        callbacks.messageDismissed(
            when (dismissType) {
                InAppMessageDismissType.CLICK -> AndroidCallbacks.InAppMessagingDismissType.CLICK
                InAppMessageDismissType.SWIPE -> AndroidCallbacks.InAppMessagingDismissType.SWIPE
                InAppMessageDismissType.AUTO -> AndroidCallbacks.InAppMessagingDismissType.AUTO
                InAppMessageDismissType.UNKNOWN -> AndroidCallbacks.InAppMessagingDismissType.UNKNOWN_DISMISS_TYPE
            }
        )
    }

    override fun displayErrorEncountered(errorReason: InAppMessageErrorReason) {
        callbacks.displayErrorEncountered(
            when (errorReason) {
                InAppMessageErrorReason.UNSPECIFIED_RENDER_ERROR -> AndroidCallbacks.InAppMessagingErrorReason.UNSPECIFIED_RENDER_ERROR
                InAppMessageErrorReason.IMAGE_FETCH_ERROR -> AndroidCallbacks.InAppMessagingErrorReason.IMAGE_FETCH_ERROR
                InAppMessageErrorReason.IMAGE_DISPLAY_ERROR -> AndroidCallbacks.InAppMessagingErrorReason.IMAGE_DISPLAY_ERROR
                InAppMessageErrorReason.IMAGE_UNSUPPORTED_FORMAT -> AndroidCallbacks.InAppMessagingErrorReason.IMAGE_UNSUPPORTED_FORMAT
            }
        )
    }
}

public fun AndroidInAppMessage.toInAppDisplayMessage(): InAppDisplayMessage {
    val campaign = campaignMetadata?.let {
        InAppMessageCampaign(
            campaignId = it.campaignId,
            campaignName = it.campaignName,
            isTestMessage = it.isTestMessage
        )
    } ?: InAppMessageCampaign("", "", false)
    val appData = data?.toMap().orEmpty()

    return when (this) {
        is AndroidCardMessage -> InAppDisplayMessage.Card(
            campaign = campaign,
            title = title.toCommon(),
            body = body?.toCommon(),
            portraitImage = portraitImageData?.toCommon(),
            landscapeImage = landscapeImageData?.toCommon(),
            primaryAction = primaryAction.toCommon(),
            secondaryAction = secondaryAction?.toCommon(),
            backgroundColor = backgroundHexColor,
            appData = appData
        )
        is AndroidBannerMessage -> InAppDisplayMessage.Banner(
            campaign = campaign,
            title = title.toCommon(),
            body = body?.toCommon(),
            image = imageData?.toCommon(),
            primaryAction = action?.toCommon(),
            backgroundColor = backgroundHexColor,
            appData = appData
        )
        is AndroidModalMessage -> InAppDisplayMessage.Modal(
            campaign = campaign,
            title = title.toCommon(),
            body = body?.toCommon(),
            image = imageData?.toCommon(),
            primaryAction = action?.toCommon(),
            backgroundColor = backgroundHexColor,
            appData = appData
        )
        is AndroidImageOnlyMessage -> InAppDisplayMessage.ImageOnly(
            campaign = campaign,
            image = imageData.toCommon(),
            primaryAction = action?.toCommon(),
            appData = appData
        )
        else -> error("지원하지 않는 Android In-App Messaging 표시 유형입니다: ${this::class.simpleName}")
    }
}

private fun AndroidInAppMessage.androidActionFor(action: InAppMessageAction?): AndroidAction? = when (this) {
    is AndroidCardMessage -> when (action) {
        secondaryAction?.toCommon() -> secondaryAction
        primaryAction.toCommon() -> primaryAction
        else -> null
    }
    is AndroidBannerMessage -> this.action.takeIf { it?.toCommon() == action }
    is AndroidModalMessage -> this.action.takeIf { it?.toCommon() == action }
    is AndroidImageOnlyMessage -> this.action.takeIf { it?.toCommon() == action }
    else -> null
}

private fun AndroidText.toCommon(): InAppMessageText = InAppMessageText(
    text = text,
    color = hexColor
)

private fun AndroidImageData.toCommon(): InAppMessageImage = InAppMessageImage(
    imageUrl = imageUrl,
    rawData = null
)

private fun AndroidButton.toCommon(): InAppMessageButton = InAppMessageButton(
    text = text.toCommon(),
    backgroundColor = buttonHexColor
)

private fun AndroidAction.toCommon(): InAppMessageAction = InAppMessageAction(
    url = actionUrl,
    button = button?.toCommon()
)
