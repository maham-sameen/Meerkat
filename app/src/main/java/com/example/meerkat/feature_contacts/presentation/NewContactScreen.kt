package com.example.meerkat.feature_contacts.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.core.presentation.SessionExpiredDialog
import com.example.meerkat.feature_contacts.presentation.component.ContactTxtField
import com.example.meerkat.feature_contacts.presentation.component.EnterEmailComp
import com.example.meerkat.feature_contacts.presentation.component.NewContactsProfile
import com.example.meerkat.feature_contacts.presentation.component.PhoneNumberComp
import com.example.meerkat.core.time_zone.presentation.TimeZoneBottomSheet
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_contacts.presentation.component.TimeZoneComp
import com.example.meerkat.feature_contacts.presentation.component.TopNewContactsBar
import com.example.meerkat.core.presentation.SearchEntriesTxtField
import com.togitech.ccp.component.TogiCountryCodePicker
import kotlinx.coroutines.launch





@RequiresApi(Build.VERSION_CODES.O)
@Preview(device = Devices.PIXEL_C, showBackground = true)
@PreviewLightDark
@Composable
fun NewContactScreenPreview() {}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewContactScreen(
    contact : ContactEntity?  = null,
    newContViewModel: NewContViewModel ,
    onSaveAndContinue : () -> Unit ,
    onBackPressed : () -> Unit ,
    onLoginAgain : () -> Unit
) {
    LaunchedEffect(contact != null) {
        contact?.let{ newContViewModel.onEvent(
            NewContactEvents.EditContact(contact))
        }
    }
    if(newContViewModel.sessionDialogState.value){
        SessionExpiredDialog(
            onDismiss = {newContViewModel.onEvent(NewContactEvents.DismissDialog)},
            onLoginAgain = onLoginAgain
        )
    }
    val nameTxtFieldState = newContViewModel.nameTxtFieldState
    val emailTxtFieldState = newContViewModel.emailTxtFieldState
    val phoneNoTxtFieldState = newContViewModel.phoneNoTxtFieldState
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState =  rememberStandardBottomSheetState(
            skipHiddenState = false,
        )
    )
    val scope = rememberCoroutineScope()

    val selectedOption = newContViewModel.selectedOption
    val searchText = newContViewModel.searchText.collectAsState()
    val isSearching = newContViewModel.isTimeZoneSearching.collectAsState()
    val timeZones = newContViewModel.timeZonesCategory.collectAsState()
    val timeZoneTxtFieldState = newContViewModel.timeZoneTxtFieldState

    val isSaveAndContinue = newContViewModel.isSaveAndContinue


    BottomSheetScaffold(
        sheetContent = {
            TimeZoneBottomSheet(
                isSearching = isSearching.value,
                timeZones = timeZones.value,
                selectedOption = selectedOption.value,
                onToggleSelection = {newContViewModel.onEvent(NewContactEvents.OnToggleSelection(it)) },
                searchField = {
                    SearchEntriesTxtField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp)
                        ,
                        hint = "Search",
                        text = searchText.value,
                        onTextChange = { newContViewModel.onEvent(NewContactEvents.OnSearchTxt(it)) } ,
                        onClear = { newContViewModel.onEvent(NewContactEvents.OnSearchTxt(""))}  ,
                        onBack = {
                            scope.launch {
                                scaffoldState.bottomSheetState.hide()
                            }
                        }
                    )
                },
                onClose = {
                    scope.launch {
                        scaffoldState.bottomSheetState.hide()
                    }
                },
                onDone = {
                    scope.launch {
                        newContViewModel.onEvent(NewContactEvents.OnDone)
                        scaffoldState.bottomSheetState.hide()
                    }
                },
            )
        } ,
        topBar = { TopNewContactsBar(
            isSaveContinue = isSaveAndContinue.value,
            onBackPressed = {
                newContViewModel.clearState()
                onBackPressed() },
            onSaveAndContinue = { if(isSaveAndContinue.value){
                newContViewModel.onEvent(NewContactEvents.OnSaveAndContinue)
                onSaveAndContinue()
                newContViewModel.clearState()
            } }
        ) },
        modifier = Modifier,
        scaffoldState = scaffoldState,
        sheetMaxWidth = BottomSheetDefaults.SheetMaxWidth,
        sheetContainerColor = MaterialTheme.colorScheme.surface
    ) {innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ){
            Box{
                NewContactsProfile(
                    firstCapitalLetterTxt = {
                        Text(
                            text = nameTxtFieldState.value.text.take(1),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.SemiBold ,
                        )
                    },
                    contactNameTxtField = {
                        ContactTxtField(
                            modifier = Modifier.heightIn(min = 70.dp),
                            fontSize = 20.sp,
                            hint = "Enter contact name",
                            containerColor = MaterialTheme.colorScheme.surface,
                            text = nameTxtFieldState.value.text,
                            onTextChanged = {newContViewModel.onEvent(NewContactEvents.EnterName(it))},
                            isError = nameTxtFieldState.value.isError,
                            isFocused = nameTxtFieldState.value.isFocus,
                            onFocusChanged = {newContViewModel.onEvent(NewContactEvents.FocusChangeNameTxtField(it))},
                        )
                    }
                )
            }
            HorizontalDivider()
            Column (
                modifier = Modifier
                    .padding(top = 20.dp , start = 20.dp)
            ){
                Text(
                    text = "DETAILS" ,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(20.dp))
                EnterEmailComp(
                    emailTxtField = {
                        ContactTxtField(
                            hint = "Enter email name",
                            text = emailTxtFieldState.value.text,
                            onTextChanged = {newContViewModel.onEvent(NewContactEvents.EnterEmail(it))},
                            isError = emailTxtFieldState.value.isError,
                            isFocused = emailTxtFieldState.value.isFocus,
                            onFocusChanged = {newContViewModel.onEvent(NewContactEvents.FocusChangeEmailTxtField(it))},
                        )
                    }
                )
                Spacer(Modifier.height(20.dp))
                PhoneNumberComp(
                    phoneNoTxtField = {
                        //https://github.com/jump-sdk/jetpack_compose_country_code_picker_emoji
                        val focusManager = LocalFocusManager.current
                        TogiCountryCodePicker(
                            initialPhoneNumber = phoneNoTxtFieldState.value.phoneNoTxtFieldState.text,
                            initialCountryPhoneCode = phoneNoTxtFieldState.value.code,
                            modifier = Modifier
                                .padding(end = 25.dp)
                                .onFocusChanged {
                                    newContViewModel.onEvent(
                                        NewContactEvents.FocusChangePhoneNoTxtField(
                                            it
                                        )
                                    )
                                }
                            ,
                            onValueChange = { (code, phone), isValid ->
                                newContViewModel.onEvent(NewContactEvents.EnterPhoneNo(
                                    phoneNo = phone,
                                    isValid = isValid,
                                    code = code
                                ))
                            },
                            shape = RoundedCornerShape(5.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colorScheme.surfaceContainer ,
                                textColor = MaterialTheme.colorScheme.onSurface ,
                                disabledTextColor = MaterialTheme.colorScheme.onSurface
                            ) ,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next
                            ) , 
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Next)
                                }
                            )
                        )
                    }
                )
                Spacer(Modifier.height(20.dp))
                TimeZoneComp(
                    timeZoneName = timeZoneTxtFieldState.value.text,
                    isFocused = timeZoneTxtFieldState.value.isFocus,
                    isError = timeZoneTxtFieldState.value.isError,
                    onFocusedChanged = {newContViewModel.onEvent(NewContactEvents.FocusChangeTimeZoneTxtField(it))},
                    onSelectTimeZone = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    },
                    onTextChanged = {
                        newContViewModel.onEvent(NewContactEvents.EnterTimeZone(it))
                    }
                )
            }
        }
    }
}




