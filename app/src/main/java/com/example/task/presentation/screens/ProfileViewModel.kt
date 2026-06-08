package com.example.task.presentation.screens

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.data.PreferenceManager
import com.example.task.domain.GlanceWidgetUpdater
import com.example.task.domain.NetworkRepository
import com.example.task.domain.WidgetUpdater
import kotlinx.coroutines.launch

class ProfileViewModel(context: Context) : ViewModel() {
    private val prefs = PreferenceManager(context)
    
    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var isNotifications by mutableStateOf(prefs.notifications)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
    var isNotToken by mutableStateOf(prefs.notToken)
        private set

    init {
        loadProfile()
    }

    fun loadProfile() {
        isLoading = true
        viewModelScope.launch {
            try {
                if (!isNotToken) {
                    val token = prefs.token ?: ""
                    if (token.isNotEmpty()) {
                        val info = NetworkRepository.userInfoGet(token)
                        if (info != null) {
                            email = info.email
                            name = info.middleName
                        }
                    }
                } else {
                    name = "Авторизуйтесь"
                    email = "чтобы видеть данные профиля"
                }
            } catch (e: Exception) {
                errorMessage = "Connection error"
                // Fallback as in original code
                name = "Эдуард"
                email = "+7 967 078-58-37"
            } finally {
                isLoading = false
            }
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        isNotifications = enabled
        prefs.notifications = enabled
    }

    fun logout(context: Context, onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val token = prefs.token ?: ""
                val tokens = NetworkRepository.userTokensGet(token)
                val tokenId = tokens?.find { it.token == token }?.id
                if (tokenId != null) {
                    NetworkRepository.deleteToken(tokenId, token)
                }
            } catch (e: Exception) {
                // ignore logout error
            }
            prefs.clearAuth()
            WidgetUpdater.update(context)
            GlanceWidgetUpdater.update(context)
            onLogoutSuccess()
        }
    }
    
    fun enterAsUser(context: Context, onSuccess: () -> Unit) {
        prefs.notToken = false
        WidgetUpdater.update(context)
        viewModelScope.launch {
            GlanceWidgetUpdater.update(context)
            onSuccess()
        }
    }
}
