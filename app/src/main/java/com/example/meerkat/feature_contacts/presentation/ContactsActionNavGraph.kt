package com.example.meerkat.feature_contacts.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.meerkat.core.presentation.sharedViewModel
import com.example.meerkat.core.util.Graph
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_contacts.presentation.component.ContactInfo
import com.example.meerkat.feature_event.presentation.util.BottomBarScreen
import kotlin.reflect.typeOf


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.contactsActionNavGraph(
    navController: NavHostController ,
    onSaveAndContinue : () -> Unit ,
    onLoginAgain : () -> Unit
){

    navigation<Graph.ContactActions>(startDestination = ContactsActionsScreen.CreateContact()){
        composable<ContactsActionsScreen.CreateContact>(
            typeMap = mapOf(typeOf<ContactEntity?>() to CustomNavType(ContactEntity.serializer()))
        ){
            val viewModel = it.sharedViewModel<NewContViewModel>(navController)
            val contact = it.toRoute<ContactsActionsScreen.CreateContact>().contactEntity
            NewContactScreen(
                contact = contact,
                newContViewModel = viewModel,
                onSaveAndContinue = onSaveAndContinue,
                onBackPressed = {
                    navController.popBackStack()
                },
                onLoginAgain = onLoginAgain,
            )
        }

        composable<ContactsActionsScreen.ContactInfo>(
            typeMap = mapOf(typeOf<ContactEntity>() to CustomNavType(ContactEntity.serializer()))
        ) {backStackEntry ->
            val contact = backStackEntry.toRoute<ContactsActionsScreen.ContactInfo>().contactEntity
            ContactInfo(
                contact = contact,
                onBackPressed = {
                    navController.popBackStack()
                },
                onEditContact = {
                    navController.navigate(ContactsActionsScreen.CreateContact(contact)) {
                        popUpTo(BottomBarScreen.Contacts) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

