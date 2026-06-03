package com.example.meerkat.feature_auth.presentation.email

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meerkat.R
import com.example.meerkat.core.presentation.UIState
import com.example.meerkat.feature_auth.domain.use_case.AuthUseCases
import com.example.meerkat.feature_auth.domain.util.PasswordStrengthLevels
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EmailViewModel
    @Inject constructor(
        private val useCases : AuthUseCases
    ): ViewModel() {
        private var _uiState = MutableSharedFlow<UIState>()
        val uiState  = _uiState.asSharedFlow()

        private var _emailText = mutableStateOf("")
        val emailText : State<String> = _emailText

        private var _nameText = mutableStateOf("")
        val nameText = _nameText

        private var _visibilityPasswordState = mutableStateOf(VisibilityPasswordState())
        val visibilityPasswordState : State<VisibilityPasswordState> = _visibilityPasswordState

        private var _passwordStrengthLevels = mutableStateOf(PasswordStrengthLevels.LOW)
        val passwordStrengthLevels : State<PasswordStrengthLevels> = _passwordStrengthLevels

       private var _introTxt = mutableStateOf("")
       val introTxt : State<String> = _introTxt

       private var currentScreen : Int = 0



    fun onEvent(event : EmailUIEvent){
        when(event){
            is EmailUIEvent.EnterEmail -> {
                _emailText.value = event.email
            }
            is EmailUIEvent.EnterPassword -> {
                _visibilityPasswordState.value = _visibilityPasswordState.value.copy(
                    password = event.password
                )
                _passwordStrengthLevels.value = useCases.checkPasswordStrength(event.password)
            }
            is EmailUIEvent.AuthWithEmail -> {
                viewModelScope.launch{
                    _uiState.emit(UIState.Loading)
                     when(currentScreen){
                        0 -> {
                            val result = useCases.signUpEmail(
                                email = _emailText.value,
                                password = _visibilityPasswordState.value.password,
                                name = _nameText.value,
                                users = useCases.getUsers()
                            )
                            if(result.errorMessage != null){
                                _uiState.emit(
                                    UIState.Error(result.errorMessage)
                                )
                            }else{
                                _uiState.emit(
                                    UIState.Success("User have registered successfully")
                                )
                            }
                        }
                        1 -> {
                            useCases.signInEmail(
                                email = _emailText.value,
                                password = _visibilityPasswordState.value.password
                            ).fold(
                                onSuccess = {
                                    _uiState.emit(
                                        UIState.Success(it)
                                    )
                                },
                                onFailure = {
                                    _uiState.emit(
                                        UIState.Error(it.message ?: "Login Failed")
                                    )
                                }
                            )
                        }
                    }
                }
            }
            is EmailUIEvent.OnToggleVisibility -> {
                val currentState = _visibilityPasswordState.value
                val newVisibility = !currentState.visibility

                _visibilityPasswordState.value =
                    currentState.copy(
                        visibility = newVisibility,
                        visualTransformation = if (newVisibility)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        painter = if (newVisibility)
                            R.drawable.baseline_visibility_off_24
                        else
                            R.drawable.baseline_visibility_24
                    )
            }
            is EmailUIEvent.IntroTxt -> {
                currentScreen = event.id
                when(event.id){
                    0 -> {
                        _introTxt.value = "sign up with calendly"
                    }
                    1 -> {
                        _introTxt.value = "sign in with calendly"
                    }
                }
            }
            is EmailUIEvent.EnterName -> {
                _nameText.value = event.name
            }
        }
    }

    suspend fun clearUiState(){
        _uiState.emit(
            UIState.Idle
        )
    }
}