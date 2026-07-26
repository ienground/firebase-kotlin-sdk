package zone.ien.firebase.inappmessaging.display

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.value
import platform.CoreGraphics.CGFloatVar
import platform.Foundation.NSError
import platform.Foundation.NSSelectorFromString
import platform.Foundation.NSURL
import platform.UIKit.UIColor
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessaging
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessagingAction
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessagingActionButton
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessagingBannerDisplay
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessagingCardDisplay
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessagingDismissType
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessagingDisplayDelegateProtocol
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessagingDisplayMessage
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessagingDisplayProtocol
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessagingErrorDomain
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessagingImageData
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessagingImageOnlyDisplay
import swiftPMImport.zone.ien.firebase.firebase.inappmessaging.FIRInAppMessagingModalDisplay
import kotlin.math.roundToInt

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseInAppMessagingDisplay private actual constructor() {
    private val delegate: FIRInAppMessaging = FIRInAppMessaging.inAppMessaging()
    private var displayComponent: IosDisplayComponent? = null

    public actual fun setCustomDisplayListener(listener: InAppMessagingDisplayListener) {
        val component = IosDisplayComponent(listener)
        delegate.messageDisplayComponent = component
        displayComponent = component
    }

    public actual fun clearCustomDisplayListener() {
        delegate.performSelector(
            NSSelectorFromString("setMessageDisplayComponent:"),
            withObject = null
        )
        displayComponent = null
    }

    public actual companion object {
        public actual val instance: FirebaseInAppMessagingDisplay = FirebaseInAppMessagingDisplay()
    }
}

@OptIn(ExperimentalForeignApi::class)
private class IosDisplayComponent(
    private val listener: InAppMessagingDisplayListener,
    private val dispatcher: IosDisplayListenerDispatcher = IosDisplayListenerDispatcher()
) : NSObject(), FIRInAppMessagingDisplayProtocol {
    override fun displayMessage(
        messageForDisplay: FIRInAppMessagingDisplayMessage,
        displayDelegate: FIRInAppMessagingDisplayDelegateProtocol
    ) {
        val message = messageForDisplay.toInAppDisplayMessage()
        val callbacks = IosDisplayCallbacks(message, messageForDisplay, displayDelegate)
        dispatcher.dispatch(
            listener = listener,
            message = message,
            callbacks = callbacks
        )
    }
}

internal class IosDisplayListenerDispatcher(
    private val dispatch: ((() -> Unit) -> Unit) = { block ->
        dispatch_async(dispatch_get_main_queue(), block)
    }
) {
    fun dispatch(
        listener: InAppMessagingDisplayListener,
        message: InAppDisplayMessage,
        callbacks: InAppMessagingDisplayCallbacks
    ) {
        dispatch {
            listener.displayMessage(message, callbacks)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class IosDisplayCallbacks(
    private val message: InAppDisplayMessage,
    private val nativeMessage: FIRInAppMessagingDisplayMessage,
    private val displayDelegate: FIRInAppMessagingDisplayDelegateProtocol
) : InAppMessagingDisplayCallbacks {
    override fun impressionDetected() {
        displayDelegate.impressionDetectedForMessage(nativeMessage)
    }

    override fun messageClicked() {
        messageClicked(message.primaryAction)
    }

    override fun messageClicked(action: InAppMessageAction?) {
        val validatedAction = message.validatedAction(action) ?: return
        val nativeAction = FIRInAppMessagingAction(
            actionText = validatedAction.button?.text?.text,
            actionURL = validatedAction.url?.let(NSURL::URLWithString)
        )
        displayDelegate.messageClicked(nativeMessage, withAction = nativeAction)
    }

    override fun messageDismissed(dismissType: InAppMessageDismissType) {
        displayDelegate.messageDismissed(
            nativeMessage,
            dismissType = dismissType.toIosDismissType()
        )
    }

    override fun displayErrorEncountered(errorReason: InAppMessageErrorReason) {
        val code: Long = when (errorReason) {
            InAppMessageErrorReason.IMAGE_FETCH_ERROR,
            InAppMessageErrorReason.IMAGE_DISPLAY_ERROR,
            InAppMessageErrorReason.IMAGE_UNSUPPORTED_FORMAT -> 0L
            InAppMessageErrorReason.UNSPECIFIED_RENDER_ERROR -> 1L
        }
        displayDelegate.displayErrorForMessage(
            nativeMessage,
            error = NSError.errorWithDomain(FIRInAppMessagingErrorDomain, code, null)
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
public fun FIRInAppMessagingDisplayMessage.toInAppDisplayMessage(): InAppDisplayMessage {
    val campaign = InAppMessageCampaign(
        campaignId = campaignInfo.messageID,
        campaignName = campaignInfo.campaignName,
        isTestMessage = campaignInfo.renderAsTestMessage
    )
    val data = appData.toStringMap()

    return when (this) {
        is FIRInAppMessagingCardDisplay -> {
            val color = textColor.toHexColorOrNull()
            InAppDisplayMessage.Card(
                campaign = campaign,
                title = InAppMessageText(title, color),
                body = body?.let { InAppMessageText(it, color) },
                portraitImage = portraitImageData.toInAppMessageImage(),
                landscapeImage = landscapeImageData?.toInAppMessageImage(),
                primaryAction = primaryActionURL.toInAppMessageAction(primaryActionButton),
                secondaryAction = secondaryActionButton?.let {
                    secondaryActionURL.toInAppMessageAction(it)
                },
                backgroundColor = displayBackgroundColor.toHexColorOrNull(),
                appData = data
            )
        }
        is FIRInAppMessagingBannerDisplay -> {
            val color = textColor.toHexColorOrNull()
            InAppDisplayMessage.Banner(
                campaign = campaign,
                title = InAppMessageText(title, color),
                body = bodyText?.let { InAppMessageText(it, color) },
                image = imageData?.toInAppMessageImage(),
                primaryAction = actionURL?.toInAppMessageAction(),
                backgroundColor = displayBackgroundColor.toHexColorOrNull(),
                appData = data
            )
        }
        is FIRInAppMessagingModalDisplay -> {
            val color = textColor.toHexColorOrNull()
            InAppDisplayMessage.Modal(
                campaign = campaign,
                title = InAppMessageText(title, color),
                body = bodyText?.let { InAppMessageText(it, color) },
                image = imageData?.toInAppMessageImage(),
                primaryAction = if (actionURL != null || actionButton != null) {
                    actionURL.toInAppMessageAction(actionButton)
                } else {
                    null
                },
                backgroundColor = displayBackgroundColor.toHexColorOrNull(),
                appData = data
            )
        }
        is FIRInAppMessagingImageOnlyDisplay -> InAppDisplayMessage.ImageOnly(
            campaign = campaign,
            image = imageData.toInAppMessageImage(),
            primaryAction = actionURL?.toInAppMessageAction(),
            appData = data
        )
        else -> error("지원하지 않는 Firebase In-App Messaging 표시 유형입니다: ${this::class.simpleName}")
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun FIRInAppMessagingImageData.toInAppMessageImage(): InAppMessageImage = InAppMessageImage(
    imageUrl = imageURL,
    rawData = imageRawData?.let { data ->
        data.bytes?.readBytes(data.length.toInt())
    }
)

@OptIn(ExperimentalForeignApi::class)
private fun NSURL?.toInAppMessageAction(
    button: FIRInAppMessagingActionButton? = null
): InAppMessageAction = InAppMessageAction(
    url = this?.absoluteString,
    button = button?.let {
        InAppMessageButton(
            text = InAppMessageText(it.buttonText, it.buttonTextColor.toHexColorOrNull()),
            backgroundColor = it.buttonBackgroundColor.toHexColorOrNull()
        )
    }
)

private fun Map<*, *>?.toStringMap(): Map<String, String> =
    this?.mapNotNull { (key, value) ->
        if (key is String && value is String) key to value else null
    }?.toMap().orEmpty()

@OptIn(ExperimentalForeignApi::class)
internal fun Any?.toHexColorOrNull(): String? {
    val color = this as? UIColor ?: return null
    return memScoped {
        val red = alloc<CGFloatVar>()
        val green = alloc<CGFloatVar>()
        val blue = alloc<CGFloatVar>()
        val alpha = alloc<CGFloatVar>()
        if (!color.getRed(red.ptr, green.ptr, blue.ptr, alpha.ptr)) return@memScoped null

        fun channel(value: Double): String =
            (value * 255).roundToInt().coerceIn(0, 255).toString(16).padStart(2, '0').uppercase()

        "#${channel(red.value)}${channel(green.value)}${channel(blue.value)}"
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun InAppMessageDismissType.toIosDismissType(): FIRInAppMessagingDismissType = when (this) {
    InAppMessageDismissType.CLICK -> FIRInAppMessagingDismissType.FIRInAppMessagingDismissTypeUserTapClose
    InAppMessageDismissType.SWIPE -> FIRInAppMessagingDismissType.FIRInAppMessagingDismissTypeUserSwipe
    InAppMessageDismissType.AUTO -> FIRInAppMessagingDismissType.FIRInAppMessagingDismissTypeAuto
    InAppMessageDismissType.UNKNOWN -> FIRInAppMessagingDismissType.FIRInAppMessagingDismissUnspecified
}
