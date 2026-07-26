package zone.ien.firebase.inappmessaging.display

import com.google.firebase.inappmessaging.model.Action
import com.google.firebase.inappmessaging.model.Button
import com.google.firebase.inappmessaging.model.CampaignMetadata
import com.google.firebase.inappmessaging.model.CardMessage
import com.google.firebase.inappmessaging.model.ImageData
import com.google.firebase.inappmessaging.model.Text
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AndroidInAppMessageMapperTest {
    @Test
    fun Android_카드_메시지의_핵심_필드를_공통_모델로_보존한다() {
        val title = text("제목", "#111111")
        val body = text("본문", "#222222")
        val primaryAction = action(
            "https://example.com/primary",
            button(text("확인", "#FFFFFF"), "#3367D6")
        )
        val secondaryAction = action(
            "https://example.com/secondary",
            button(text("취소", "#000000"), "#DDDDDD")
        )
        val campaign = CampaignMetadata("campaign-1", "캠페인", true)
        val portraitImage = ImageData("https://example.com/portrait.png", null)
        val landscapeImage = ImageData("https://example.com/landscape.png", null)
        val androidMessage = CardMessage::class.java.getDeclaredConstructor(
            CampaignMetadata::class.java,
            Text::class.java,
            Text::class.java,
            ImageData::class.java,
            ImageData::class.java,
            String::class.java,
            Action::class.java,
            Action::class.java,
            Map::class.java
        ).apply { isAccessible = true }.newInstance(
            campaign,
            title,
            body,
            portraitImage,
            landscapeImage,
            "#FFFFFF",
            primaryAction,
            secondaryAction,
            mapOf("route" to "inbox")
        )

        val message = assertIs<InAppDisplayMessage.Card>(androidMessage.toInAppDisplayMessage())

        assertEquals("campaign-1", message.campaign.campaignId)
        assertEquals(true, message.campaign.isTestMessage)
        assertEquals("제목", message.title.text)
        assertEquals("#111111", message.title.color)
        assertEquals("본문", message.body?.text)
        assertEquals("https://example.com/portrait.png", message.portraitImage?.imageUrl)
        assertEquals("https://example.com/landscape.png", message.landscapeImage?.imageUrl)
        assertEquals("https://example.com/primary", message.primaryAction.url)
        assertEquals("확인", message.primaryAction.button?.text?.text)
        assertEquals("https://example.com/secondary", message.secondaryAction?.url)
        assertEquals("#FFFFFF", message.backgroundColor)
        assertEquals("inbox", message.appData["route"])
    }

    private fun text(value: String, color: String): Text = Text::class.java
        .getDeclaredConstructor(String::class.java, String::class.java)
        .apply { isAccessible = true }
        .newInstance(value, color)

    private fun button(text: Text, color: String): Button = Button::class.java
        .getDeclaredConstructor(Text::class.java, String::class.java)
        .apply { isAccessible = true }
        .newInstance(text, color)

    private fun action(url: String, button: Button): Action = Action::class.java
        .getDeclaredConstructor(String::class.java, Button::class.java)
        .apply { isAccessible = true }
        .newInstance(url, button)
}
