package zone.ien.firebase.inappmessaging.display

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDate
import platform.Foundation.NSRunLoop
import platform.Foundation.NSThread
import platform.Foundation.dateWithTimeIntervalSinceNow
import platform.Foundation.runUntilDate
import platform.Foundation.timeIntervalSinceNow
import platform.UIKit.UIColor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalForeignApi::class)
class IosDisplayUtilitiesTest {
    @Test
    fun iOS_색상은_alpha를_제외한_RRGGBB로_정규화한다() {
        val color = UIColor.colorWithRed(
            red = 0.1,
            green = 0.5,
            blue = 1.0,
            alpha = 0.25
        )

        assertEquals("#1A80FF", color.toHexColorOrNull())
    }

    @Test
    fun testDisplayListenerNotCalledUntilDispatcherExecutes() {
        var scheduled: (() -> Unit)? = null
        var listenerCalled = false
        val dispatcher = IosDisplayListenerDispatcher { block -> scheduled = block }
        val message = InAppDisplayMessage.Banner(
            campaign = InAppMessageCampaign("campaign-1", "캠페인", false),
            title = InAppMessageText("제목", "#000000")
        )
        val callbacks = NoOpCallbacks()
        val listener = object : InAppMessagingDisplayListener {
            override fun displayMessage(
                message: InAppDisplayMessage,
                callbacks: InAppMessagingDisplayCallbacks
            ) {
                listenerCalled = true
            }
        }

        dispatcher.dispatch(listener, message, callbacks)

        assertFalse(listenerCalled)
        scheduled?.invoke()
        assertTrue(listenerCalled)
    }

    @Test
    fun testDefaultDispatcherCallsDisplayListenerOnMainThread() {
        var listenerCalled = false
        var calledOnMainThread = false
        val dispatcher = IosDisplayListenerDispatcher()
        val message = InAppDisplayMessage.Banner(
            campaign = InAppMessageCampaign("campaign-1", "캠페인", false),
            title = InAppMessageText("제목", "#000000")
        )
        val listener = object : InAppMessagingDisplayListener {
            override fun displayMessage(
                message: InAppDisplayMessage,
                callbacks: InAppMessagingDisplayCallbacks
            ) {
                calledOnMainThread = NSThread.isMainThread
                listenerCalled = true
            }
        }

        dispatcher.dispatch(listener, message, NoOpCallbacks())
        val timeout = NSDate.dateWithTimeIntervalSinceNow(1.0)
        while (!listenerCalled && timeout.timeIntervalSinceNow > 0.0) {
            NSRunLoop.mainRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(0.01))
        }

        assertTrue(listenerCalled)
        assertTrue(calledOnMainThread)
    }

    private class NoOpCallbacks : InAppMessagingDisplayCallbacks {
        override fun impressionDetected() = Unit
        override fun messageDismissed(dismissType: InAppMessageDismissType) = Unit
        override fun displayErrorEncountered(errorReason: InAppMessageErrorReason) = Unit
    }
}
