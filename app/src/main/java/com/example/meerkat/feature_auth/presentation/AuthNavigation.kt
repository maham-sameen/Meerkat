package com.example.meerkat.feature_auth.presentation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.meerkat.core.presentation.sharedViewModel
import com.example.meerkat.core.util.Graph
import com.example.meerkat.feature_auth.presentation.auth.AuthUIScreen
import com.example.meerkat.feature_auth.presentation.email.EmailUIScreen
import com.example.meerkat.feature_auth.presentation.email.EmailUIEvent
import com.example.meerkat.feature_auth.presentation.email.EmailViewModel
import com.example.meerkat.feature_auth.presentation.intro.IntroEvents
import com.example.meerkat.feature_auth.presentation.intro.IntroScreen
import com.example.meerkat.feature_auth.presentation.util.AuthScreen
import com.example.meerkat.feature_auth.presentation.intro.CurrentScreen


fun NavGraphBuilder.authNavigation(
    navController : NavHostController ,
    goHome : () -> Unit
) {
    navigation<Graph.Authentication>(startDestination = AuthScreen.IntroScr){
        composable<AuthScreen.IntroScr>{
            val viewModel = it.sharedViewModel<AuthViewModel>(navController)
            IntroScreen(
                createAccount = {
                    viewModel.introScreenEvent(IntroEvents.SignUp)
                    navController.navigate(AuthScreen.AuthScr){
                        popUpTo(AuthScreen.AuthScr){
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                signIn = {
                    viewModel.introScreenEvent(IntroEvents.SignIn)
                    navController.navigate(AuthScreen.AuthScr){
                        popUpTo(AuthScreen.AuthScr){
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<AuthScreen.AuthScr>{
            val viewModel = it.sharedViewModel<AuthViewModel>(navController)
            AuthUIScreen(
                viewModel,
                onAction = {currentScreen ->
                    when(currentScreen){
                        is CurrentScreen.SIGN_IN -> {
                            viewModel.introScreenEvent(IntroEvents.SignUp)

                        }
                        is CurrentScreen.SIGN_UP ->{
                            viewModel.introScreenEvent(IntroEvents.SignIn)
                        }
                    }
                },
                authWithEmail = { currentScreen , email ->
                    println("\uD83E\uDD76  AuthNavigation \n" +
                            "currentScreen $currentScreen \n" +
                            "email : $email")
                    var id = when(currentScreen){
                        is CurrentScreen.SIGN_IN -> {
                            1
                        }
                        else -> 0
                    }
                    navController.navigate(AuthScreen.EmailScr(
                        emailTxt = email,
                        currentScreen = id
                    ))
                } ,
                goHome =  goHome
            )
        }
        composable<AuthScreen.EmailScr>{
              val args = it.toRoute<AuthScreen.EmailScr>()
              val emailViewModel = hiltViewModel<EmailViewModel>(it)
              emailViewModel.onEvent(EmailUIEvent.EnterEmail(args.emailTxt))
              emailViewModel.onEvent(EmailUIEvent.IntroTxt(args.currentScreen))
               EmailUIScreen(emailViewModel)
        }
    }
}

/**
 *
 *
 * Email auth is just with firebase
 * google auth with O auth 2 authentication in which we access the user
 * credential
 *
 *
 *
 * Now when we book meeting
 * we get the time slot according to available times  in google calendar of user
 *
 *
 *
 *
 * on 10 november 1 to 2 pm is not available as we have created the meeting
 *
 *
 *
 *
 * meeting could be one to one or one to many
 *
 * now i have guest freak , Naeem that meeting will created
 * in google calendar of the guest and also in user
 *
 * when we create meeting email will vbe sent to guest by firebase .
 *
 *
 *
 * now we can update the meeting or cancel the meeting see now
 *
 *
 * ohh i forget when we send email to guest when we create meeting that is
 * done on work manager .
 *
 * I have cleans app architecture so i want to show the work manager with that
 * then we go to update and cancel meeting .
 *
 * I have use the android docs
 *
 * ok let go
 *
 * this is perfect google meets
 *
 *
 * now we are updating it
 * i just remove the guest and other things are as it is
 *
 * one more things
 * when we select the time for the google meetings then
 * that time when we send email to guest that time will be convert according
 * to their time zone see
 *
 * maaz has time zone rio_gallegos
 * our meeting zone i forget let see
 *
 * so i just change the time zone date is still same 10 nov
 *
 * now user time zone is dubai , maaz is rio_gallegos
 *
 * now meetings is on 10 am
 * now time will be convert from dubai 10 am to according to rio-gallegos for maaz according to 10
 * am see email of maaz
 *
 * so 10 am dubai time in maaz time is 2 am that is perfect so now see
 * is our meeting update or not
 * as our guest should change form 2 to one
 *
 * perfect it work now let cancel the meeting and
 * that meeting should be remove from the google calendar
 * of the user and guest see it is working now
 * i will repeat the whole process .
 *
 *
 * 12 nov tokyo 9.00 am
 * we remove maaz see
 * ok let cancel it
 *
 *
 *
 *
 *
 * Thanks alot for watching :-) 
 */

