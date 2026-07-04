package zone.ien.firebase.ai

public expect class GenerativeModel {
    public suspend fun generateContent(prompt: String): GenerateContentResponse
}
