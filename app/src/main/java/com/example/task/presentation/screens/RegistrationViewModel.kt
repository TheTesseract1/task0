package com.example.task.presentation.screens

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.data.PreferenceManager
import com.example.task.data.UpdateProfileModel
import com.example.task.domain.GlanceWidgetUpdater
import com.example.task.domain.LoginUseCasesImpl
import com.example.task.domain.NetworkRepository
import com.example.task.domain.RegUseCaseImpl
import com.example.task.domain.WidgetUpdater
import kotlinx.coroutines.launch

class RegistrationViewModel(context: Context) : ViewModel() {
    private val prefs = PreferenceManager(context)
    private val regUseCase = RegUseCaseImpl()
    private val loginUseCase = LoginUseCasesImpl()

    var isPasswordMode by mutableStateOf(false)
    var name by mutableStateOf("")
    var surname by mutableStateOf("")
    var fatherName by mutableStateOf("")
    var birthDay by mutableStateOf("")
    var gender by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordRepeat by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun nextStep() {
        isPasswordMode = true
    }

    fun register(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            var isSuccessReg = false
            regUseCase.reg(
                email, name, fatherName, surname, birthDay, gender, password, passwordRepeat,
                onFailed = { errorMessage = it },
                onSuccess = { isSuccessReg = true },
                onLoading = { isLoading = it },
                context = context
            )

            if (isSuccessReg) {
                val userInfo = UpdateProfileModel(
                    birthday = birthDay,
                    middleName = surname,
                    lastName = fatherName,
                    firstName = name,
                    gender = gender,
                    telegram = email
                )
                NetworkRepository.userInfoAdd(userInfo)
                
                loginUseCase.login(
                    email = email,
                    password = passwordRepeat,
                    context = context,
                    onLoading = { isLoading = it },
                    onFailed = { errorMessage = it },
                    onSuccess = {
                        prefs.clearAuth() // Clear any old data
                        // LoginUseCasesImpl should have saved the token now.
                        WidgetUpdater.update(context)
                        viewModelScope.launch {
                            GlanceWidgetUpdater.update(context)
                        }
                        onSuccess()
                    }
                )
            } else {
                isLoading = false
            }
        }
    }
}
