package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.local.NovaDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.GeneratedMedia
import com.example.data.model.SavedFile
import com.example.data.repository.NovaRepository
import com.example.voice.VoiceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NovaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application.applicationContext,
        NovaDatabase::class.java,
        "nova_ai_db"
    ).build()

    private val repository = NovaRepository(db.appDao())
    val voiceManager = VoiceManager(application.applicationContext)

    // Current Navigation Tab Index (0: Chat, 1: Code, 2: Media, 3: Files)
    val selectedTab = MutableStateFlow(0)

    // --- Chat State ---
    val chatMessages: StateFlow<List<ChatMessage>> = repository.getChatMessages("default_session")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatInput = MutableStateFlow("")
    val isChatLoading = MutableStateFlow(false)

    // --- Code Studio State ---
    val codePrompt = MutableStateFlow("")
    val codeLanguage = MutableStateFlow("Kotlin")
    val generatedCode = MutableStateFlow("")
    val isCodeLoading = MutableStateFlow(false)

    // --- Media Studio State ---
    val mediaPrompt = MutableStateFlow("")
    val mediaType = MutableStateFlow("IMAGE") // IMAGE or VIDEO
    val mediaStyle = MutableStateFlow("Cyberpunk")
    val generatedMediaList: StateFlow<List<GeneratedMedia>> = repository.getAllGeneratedMedia()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val isMediaLoading = MutableStateFlow(false)

    // --- File Vault State ---
    val savedFiles: StateFlow<List<SavedFile>> = repository.getAllSavedFiles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val selectedFileForView = MutableStateFlow<SavedFile?>(null)

    init {
        // Observe speech recognition results
        viewModelScope.launch {
            voiceManager.spokenText.collect { text ->
                if (text.isNotBlank()) {
                    when (selectedTab.value) {
                        0 -> {
                            chatInput.value = text
                            sendChatMessage()
                        }
                        1 -> codePrompt.value = text
                        2 -> mediaPrompt.value = text
                    }
                }
            }
        }
    }

    // --- Chat Functions ---
    fun sendChatMessage() {
        val prompt = chatInput.value.trim()
        if (prompt.isBlank() || isChatLoading.value) return

        chatInput.value = ""
        isChatLoading.value = true

        viewModelScope.launch {
            val responseMsg = repository.processChatPrompt(prompt)
            isChatLoading.value = false

            // Speak out short responses if desired
            if (responseMsg.content.length < 250) {
                voiceManager.speak(responseMsg.content)
            }
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearChatHistory()
        }
    }

    fun speakText(text: String) {
        voiceManager.speak(text)
    }

    fun stopSpeaking() {
        voiceManager.stopSpeaking()
    }

    fun startVoiceInput() {
        voiceManager.startListening { text ->
            chatInput.value = text
        }
    }

    // --- Code Studio Functions ---
    fun generateCode() {
        val prompt = codePrompt.value.trim()
        val lang = codeLanguage.value
        if (prompt.isBlank() || isCodeLoading.value) return

        isCodeLoading.value = true
        viewModelScope.launch {
            val (fullText, extracted, _) = repository.generateCodeStudio(prompt, lang)
            generatedCode.value = extracted
            isCodeLoading.value = false
        }
    }

    // --- Media Studio Functions ---
    fun generateMedia() {
        val prompt = mediaPrompt.value.trim()
        if (prompt.isBlank() || isMediaLoading.value) return

        isMediaLoading.value = true
        viewModelScope.launch {
            if (mediaType.value == "IMAGE") {
                repository.generateImageStudio(prompt, mediaStyle.value)
            } else {
                repository.generateVideoStudio(prompt)
            }
            isMediaLoading.value = false
        }
    }

    fun deleteMedia(id: Long) {
        viewModelScope.launch {
            repository.deleteMedia(id)
        }
    }

    // --- File Vault Functions ---
    fun saveCustomFile(fileName: String, fileExtension: String, content: String) {
        viewModelScope.launch {
            repository.saveFile(fileName, fileExtension, content)
        }
    }

    fun deleteFile(id: Long) {
        viewModelScope.launch {
            repository.deleteSavedFile(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceManager.destroy()
    }
}
