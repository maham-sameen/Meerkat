package com.example.meerkat.feature_auth.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meerkat.Constants
import com.example.meerkat.core.presentation.ErrorMessageTextFieldState
import com.example.meerkat.core.presentation.UIState
import com.example.meerkat.feature_auth.domain.use_case.AuthUseCases
import com.example.meerkat.feature_auth.presentation.auth.AuthEvents
import com.example.meerkat.feature_auth.presentation.auth.AuthLangState
import com.example.meerkat.feature_auth.presentation.intro.CurrentScreen
import com.example.meerkat.feature_auth.presentation.intro.IntroEvents
import com.example.meerkat.feature_auth.presentation.util.AuthConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class AuthViewModel
    @Inject constructor(
        private val useCases: AuthUseCases
    ): ViewModel() {

    private var _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState  = _uiState.asStateFlow()

    fun clearUiState() {
        viewModelScope.launch {
            _uiState.emit(UIState.Idle)
        }
    }

    // -------------------------
    // LOGIN STATE
    // -------------------------

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    // -------------------------
    // SCREEN STATE
    // -------------------------

    private var _currentScreen : MutableStateFlow<CurrentScreen> = MutableStateFlow(CurrentScreen.SIGN_IN)
    val currentScreen : StateFlow<CurrentScreen> = _currentScreen.asStateFlow()

    // -------------------------
    // EMAIL STATE
    // -------------------------

    private val _emailAddress = mutableStateOf(
        ErrorMessageTextFieldState()
    )
    val emailAddress : State<ErrorMessageTextFieldState> = _emailAddress

    // -------------------------
    // LANGUAGE STATE (Dummy for now)
    // -------------------------

    private var _authLangState = mutableStateOf(
        AuthLangState()
    )
    val authLangState : State<AuthLangState>  = _authLangState

    fun introScreenEvent(event : IntroEvents){
        when(event){
            IntroEvents.SignIn -> {
                Timber.tag(Constants.appName).i("[AuthViewModel] introScreenEvent , signIn")
                _currentScreen.value = CurrentScreen.SIGN_IN
                _authLangState.value = AuthConstant.englishSignIn
            }
            IntroEvents.SignUp -> {
                Timber.tag(Constants.appName).i("[AuthViewModel]  introScreenEvent , signUp")
                _currentScreen.value = CurrentScreen.SIGN_UP
                _authLangState.value = AuthConstant.englishSignUp
            }
        }
        _emailAddress.value = ErrorMessageTextFieldState()
    }

    fun authScreenEvent(event : AuthEvents){
        when(event){
            is AuthEvents.HandleAuth -> {
                viewModelScope.launch {
                    Timber.tag(Constants.appName).i("[AuthViewModel]  Show Loading state")
                    _uiState.value = UIState.Loading
                    val result = useCases.handleAuthorization(
                            intent = event.intent,
                            users = useCases.getUsers()
                        )
                    if(result.data!= null){
                        Timber.tag(Constants.appName).i("[AuthViewModel]  isLoggedIn is true")
                        _isLoggedIn.value = true
                        Timber.tag(Constants.appName).i("[AuthViewModel]  UIState is success")
                        _uiState.value = UIState.Success()
                    }else{
                        Timber.tag(Constants.appName).i("[AuthViewModel]  UIState is failure")
                        _uiState.value = UIState.Error(result.errorMessage!!)
                    }
                }
            }
            is AuthEvents.StartWithGoogle -> {
                viewModelScope.launch{
                    Timber.tag(Constants.appName).i("[AuthViewModel]  Show loading to auth with google ")
                    _uiState.value = UIState.Loading
                    useCases.authGoogleClient(
                        context = event.context,
                        onLaunch ={ viewModelScope.launch{ _uiState.value = UIState.NoAccount(it) }}
                    )
                }
            }
            is AuthEvents.EnterEmailAddress -> {
                _emailAddress.value = _emailAddress.value.copy(
                    text = event.value,
                    isError = !useCases.isValidEmailAddress(event.value)
                )
            }
            is AuthEvents.StartWithMicrosoft -> {}
            is AuthEvents.EmailAddressValidation -> {
                _emailAddress.value = _emailAddress.value.copy(
                    isError = !useCases.isValidEmailAddress(_emailAddress.value.text)
                )
            }
        }
    }
}