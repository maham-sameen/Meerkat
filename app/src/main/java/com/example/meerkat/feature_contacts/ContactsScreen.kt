package com.example.meerkat.feature_contacts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meerkat.R
import com.example.meerkat.core.presentation.SessionExpiredDialog
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_contacts.presentation.NewContViewModel
import com.example.meerkat.feature_contacts.presentation.NewContactEvents
import com.example.meerkat.feature_contacts.presentation.component.Contacts
import com.example.meerkat.core.presentation.NoEntries
import com.example.meerkat.core.presentation.SearchEntriesTxtField
import com.example.meerkat.core.presentation.topAppBarColors
import com.example.meerkat.ui.theme.blue400

@RequiresApi(Build.VERSION_CODES.O)
@Preview(device = Devices.PIXEL_C, showBackground = true)
@PreviewLightDark
@Composable
fun ContactScreenPreview() {
    val viewModel = hiltViewModel<NewContViewModel>()
    ContactsScreen(viewModel  , {} , {item -> } , {})
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContactsScreen(
    viewModel: NewContViewModel ,
    onAddContact: () -> Unit ,
    onClickContact : (item : ContactEntity) -> Unit ,
    onLoginAgain : () -> Unit
) {
    Scaffold (
        topBar = {
            TopContactsBar(
                onAddContact = onAddContact
            )
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            val allContacts = viewModel.allContacts.collectAsState()
            val isSearching = viewModel.isSearching

            when {
                isSearching.value -> {
                    CircularProgressIndicator()
                }

                viewModel.sessionDialogState.value -> {
                    SessionExpiredDialog(
                        onDismiss = { viewModel.onEvent(NewContactEvents.DismissDialog) },
                        onLoginAgain = onLoginAgain
                    )
                }

                allContacts.value.isEmpty() -> {
                    NoEntries(
                        modifier = Modifier,
                        image = R.drawable.contacts,
                        txt1 = "No contacts have been added yet",
                        txt2 = "Contacts will be added automatically when you schedule new meetings"
                    )
                }

                else -> {
                    Contacts(
                        modifier = Modifier,
                        list = allContacts.value,
                        onClick = onClickContact
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopContactsBar(
    onAddContact : () -> Unit
) {
    TopAppBar(
        title = {
            var text by remember { mutableStateOf("") }
            SearchEntriesTxtField(
                modifier = Modifier
                    .padding(bottom = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                hint = "Search contacts...",
                text = text,
                onTextChange = { text = it }
            )
        },
        colors = topAppBarColors(),
        modifier = Modifier.heightIn(min = 74.dp) ,
        actions = {
            Icon(
                painter = painterResource(R.drawable.outline_person_add_24) ,
                contentDescription = "add contact" ,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .fillMaxHeight()
                    .width(30.dp)
                    .clickable {
                        onAddContact()
                    }
                    .align(Alignment.Bottom) ,
                tint = blue400
            )
        }
    )
}