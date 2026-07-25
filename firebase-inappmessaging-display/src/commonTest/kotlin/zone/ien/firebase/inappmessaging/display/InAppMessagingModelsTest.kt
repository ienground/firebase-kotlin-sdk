package zone.ien.firebase.inappmessaging.display

import kotlin.test.Test
import kotlin.test.assertEquals

class InAppMessagingModelsTest {
    @Test
    fun 메시지_메타데이터를_값으로_비교한다() {
        val metadata = InAppMessageMetadata("campaign-1", "신규 사용자", "BANNER")

        assertEquals(metadata, metadata.copy())
        assertEquals("campaign-1", metadata.campaignId)
    }

    @Test
    fun 표시_리스너는_메시지와_콜백을_전달한다() {
        val events = mutableListOf<String>()
        val callbacks = object : InAppMessagingDisplayCallbacks {
            override fun impressionDetected() {
                events += "노출"
            }

            override fun messageClicked() {
                events += "클릭"
            }

            override fun messageDismissed(dismissType: InAppMessageDismissType) {
                events += "닫기:${dismissType.name}"
            }

            override fun displayErrorEncountered(errorReason: InAppMessageErrorReason) {
                events += "오류:${errorReason.name}"
            }
        }
        val listener = object : InAppMessagingDisplayListener {
            override fun displayMessage(
                message: InAppMessageMetadata,
                callbacks: InAppMessagingDisplayCallbacks
            ) {
                events += "표시:${message.campaignId}"
                callbacks.impressionDetected()
                callbacks.messageClicked()
                callbacks.messageDismissed(InAppMessageDismissType.SWIPE)
                callbacks.displayErrorEncountered(InAppMessageErrorReason.IMAGE_FETCH_ERROR)
            }
        }

        listener.displayMessage(InAppMessageMetadata("campaign-1", "캠페인", "CARD"), callbacks)

        assertEquals(
            listOf("표시:campaign-1", "노출", "클릭", "닫기:SWIPE", "오류:IMAGE_FETCH_ERROR"),
            events
        )
    }
}
