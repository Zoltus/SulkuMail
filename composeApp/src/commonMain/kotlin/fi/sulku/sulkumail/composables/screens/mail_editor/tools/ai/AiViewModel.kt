package fi.sulku.sulkumail.composables.screens.mail_editor.tools.ai

import SulkuMail.shared.BuildConfig
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.sulku.sulkumail.AiInputRequest
import fi.sulku.sulkumail.AiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class AiViewModel : ViewModel() {

    private val client = HttpClient {
        expectSuccess = false // To handle 4xx/5xx responses //todo
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    private val _responses = MutableStateFlow<List<String>>(emptyList())
    val responses = _responses.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun askAi(prompt: String) {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val aiResponse = askPrompt(prompt)
                _responses.value = _responses.value + aiResponse

            } catch (e: Exception) {
                _responses.value = _responses.value + "Failed to get response: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun askPrompt(prompt: String): String {
        return try {
            client.post(BuildConfig.BACKEND_URL + "/api/ai/write") {
                contentType(ContentType.Application.Json)
                setBody(AiInputRequest(prompt))
            }.body<AiResponse>().value
        } catch (e: Exception) {
            "Exception: ${e.message}"
        }
    }
}