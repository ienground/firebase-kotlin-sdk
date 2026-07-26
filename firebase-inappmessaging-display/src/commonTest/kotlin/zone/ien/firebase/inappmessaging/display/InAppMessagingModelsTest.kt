package zone.ien.firebase.inappmessaging.display

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

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

    @Test
    fun 카드_메시지는_렌더링에_필요한_모든_공통_필드를_보존한다() {
        val primaryAction = InAppMessageAction(
            url = "https://example.com/primary",
            button = InAppMessageButton(
                text = InAppMessageText("확인", "#FFFFFF"),
                backgroundColor = "#3367D6"
            )
        )
        val message: InAppDisplayMessage = InAppDisplayMessage.Card(
            campaign = InAppMessageCampaign("campaign-1", "신규 사용자", true),
            title = InAppMessageText("제목", "#111111"),
            body = InAppMessageText("본문", "#222222"),
            portraitImage = InAppMessageImage("https://example.com/portrait.png"),
            landscapeImage = InAppMessageImage("https://example.com/landscape.png"),
            primaryAction = primaryAction,
            secondaryAction = InAppMessageAction(url = "https://example.com/secondary"),
            backgroundColor = "#FFFFFF",
            appData = mapOf("route" to "inbox")
        )

        val card = assertIs<InAppDisplayMessage.Card>(message)
        assertEquals(InAppMessageType.CARD, card.messageType)
        assertEquals("campaign-1", card.campaign.campaignId)
        assertEquals(true, card.campaign.isTestMessage)
        assertEquals("제목", card.title.text)
        assertEquals("https://example.com/portrait.png", card.portraitImage?.imageUrl)
        assertEquals("https://example.com/landscape.png", card.landscapeImage?.imageUrl)
        assertEquals(primaryAction, card.primaryAction)
        assertEquals("inbox", card.appData["route"])
    }

    @Test
    fun 배너_모달_이미지전용_메시지를_구분한다() {
        val campaign = InAppMessageCampaign("campaign-1", "캠페인", false)
        val banner: InAppDisplayMessage = InAppDisplayMessage.Banner(
            campaign = campaign,
            title = InAppMessageText("배너", "#000000")
        )
        val modal: InAppDisplayMessage = InAppDisplayMessage.Modal(
            campaign = campaign,
            title = InAppMessageText("모달", "#000000")
        )
        val imageOnly: InAppDisplayMessage = InAppDisplayMessage.ImageOnly(
            campaign = campaign,
            image = InAppMessageImage("https://example.com/image.png")
        )

        assertEquals(InAppMessageType.BANNER, banner.messageType)
        assertEquals(InAppMessageType.MODAL, modal.messageType)
        assertEquals(InAppMessageType.IMAGE_ONLY, imageOnly.messageType)
        assertNull(imageOnly.title)
    }

    @Test
    fun 클릭_콜백은_실제_action을_전달하면서_기존_noarg_구현도_호환한다() {
        val events = mutableListOf<String>()
        val action = InAppMessageAction(url = "https://example.com")
        val legacyCallbacks = object : InAppMessagingDisplayCallbacks {
            override fun messageClicked() {
                events += "legacy"
            }

            override fun impressionDetected() = Unit
            override fun messageDismissed(dismissType: InAppMessageDismissType) = Unit
            override fun displayErrorEncountered(errorReason: InAppMessageErrorReason) = Unit
        }
        val typedCallbacks = object : InAppMessagingDisplayCallbacks {
            override fun messageClicked(action: InAppMessageAction?) {
                events += action?.url ?: "none"
            }

            override fun impressionDetected() = Unit
            override fun messageDismissed(dismissType: InAppMessageDismissType) = Unit
            override fun displayErrorEncountered(errorReason: InAppMessageErrorReason) = Unit
        }

        legacyCallbacks.messageClicked(action)
        typedCallbacks.messageClicked(action)

        assertEquals(listOf("legacy", "https://example.com"), events)
    }

    @Test
    fun 클릭은_메시지에_포함된_action만_허용하고_null은_무시한다() {
        val primary = InAppMessageAction(url = "https://example.com/primary")
        val message = InAppDisplayMessage.Banner(
            campaign = InAppMessageCampaign("campaign-1", "캠페인", false),
            title = InAppMessageText("제목", null),
            primaryAction = primary
        )

        assertEquals(primary, message.validatedAction(primary))
        assertNull(message.validatedAction(null))
        assertNull(message.validatedAction(InAppMessageAction(url = "https://example.com/other")))
    }
}
