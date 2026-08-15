package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// --- Room Entities ---

enum class MessageSender {
    USER, AI
}

enum class MessageType {
    TEXT, CODE, IMAGE, VIDEO, FILE
}

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: String = "default_session",
    val sender: MessageSender,
    val messageType: MessageType = MessageType.TEXT,
    val content: String,
    val codeSnippet: String? = null,
    val codeLanguage: String? = null,
    val mediaUrl: String? = null,
    val filePath: String? = null,
    val fileName: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_files")
data class SavedFile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fileName: String,
    val fileExtension: String, // kt, py, json, md, html, csv, txt
    val content: String,
    val sizeBytes: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val tags: String = "AI Generated"
)

@Entity(tableName = "generated_media")
data class GeneratedMedia(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mediaType: String, // IMAGE or VIDEO
    val prompt: String,
    val mediaUrl: String, // Base64 or local URI or URL
    val style: String = "Cyberpunk",
    val aspectRatio: String = "1:1",
    val createdAt: Long = System.currentTimeMillis()
)

// --- Gemini REST API Request & Response Data Classes (Moshi annotated) ---

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @Json(name = "text") val text: String? = null,
    @Json(name = "inlineData") val inlineData: GeminiInlineData? = null
)

@JsonClass(generateAdapter = true)
data class GeminiInlineData(
    @Json(name = "mimeType") val mimeType: String,
    @Json(name = "data") val data: String
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @Json(name = "parts") val parts: List<GeminiPart>,
    @Json(name = "role") val role: String? = "user"
)

@JsonClass(generateAdapter = true)
data class GeminiImageConfig(
    @Json(name = "aspectRatio") val aspectRatio: String? = "1:1",
    @Json(name = "imageSize") val imageSize: String? = "1K"
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    @Json(name = "temperature") val temperature: Float? = 0.7f,
    @Json(name = "topP") val topP: Float? = 0.95f,
    @Json(name = "topK") val topK: Int? = 40,
    @Json(name = "responseModalities") val responseModalities: List<String>? = null,
    @Json(name = "imageConfig") val imageConfig: GeminiImageConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @Json(name = "contents") val contents: List<GeminiContent>,
    @Json(name = "generationConfig") val generationConfig: GeminiGenerationConfig? = null,
    @Json(name = "systemInstruction") val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidateContent(
    @Json(name = "parts") val parts: List<GeminiPart>?
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @Json(name = "content") val content: GeminiCandidateContent?
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @Json(name = "candidates") val candidates: List<GeminiCandidate>?
)
