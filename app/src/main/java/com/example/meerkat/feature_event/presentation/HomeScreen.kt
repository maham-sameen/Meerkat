// Optimized & Cleaned-up HomeScreen.kt
package com.example.meerkat.feature_event.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meerkat.R
import com.example.meerkat.core.presentation.*
import com.example.meerkat.feature_event.presentation.component.EventType
import com.example.meerkat.feature_meetings.presentation.TopBar
import com.example.meerkat.feature_event.presentation.component.NotificationPermissionTextProvider
import com.example.meerkat.feature_event.presentation.component.PermissionDialog
import com.example.meerkat.ui.theme.blue400


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onPickImage: () -> Unit,
    onBookMeeting: () -> Unit,
    onCreateNewContact: () -> Unit
) {
    val showSheet = remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val activity = context as? Activity
    val dialogStack = homeViewModel.visiblePermissionDialogStack
    var launchPermissionRequest by remember { mutableStateOf<(() -> Unit)?>(null) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionRequest = arrayOf(Manifest.permission.POST_NOTIFICATIONS)

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissionRequest.forEach { permission ->
                homeViewModel.onEvent(
                    HomeEvents.OnPermissionResult(
                        permission = permission,
                        isGranted = permissions[permission] == true
                    )
                )
                if(permissions[Manifest.permission.POST_NOTIFICATIONS] == true){
                    onBookMeeting()
                }
            }
        }
        LaunchedEffect(Unit) {
            launchPermissionRequest = {
                permissionLauncher.launch(permissionRequest)
            }
        }

        dialogStack.forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.POST_NOTIFICATIONS -> NotificationPermissionTextProvider()
                    else -> return@forEach
                },
                isPermanentlyDeclined = activity?.let {
                    !ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
                } ?: false,
                onDismiss = { homeViewModel.onEvent(HomeEvents.DismissDialog) },
                onOkClick = {
                    homeViewModel.onEvent(HomeEvents.DismissDialog)
                    permissionLauncher.launch(arrayOf(permission))
                },
                onGoToAppSettingsClick = {
                    activity?.openAppSettings()
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopBar(modifier = Modifier.heightIn(min = 87.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    var text by remember { mutableStateOf("") }
                    SearchEntriesTxtField(
                        modifier = Modifier.weight(1f),
                        hint = "Search event type..",
                        text = text,
                        onTextChange = { text = it }
                    )
                    Spacer(Modifier.width(10.dp))
                    DefaultImage(
                        modifier = Modifier.size(50.dp),
                        onPickImage = onPickImage
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet.value = true },
                containerColor = blue400,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(70.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            if (homeViewModel.showEntriesType.value) {
                NoEntries(
                    modifier = Modifier,
                    image = R.drawable.events,
                    txt1 = "You have no events types",
                    txt2 = "Event types are used to set up meeting details for events that you will schedule regularly, like a product demo, customer call or office hours"
                )
            } else {
                EventType()
            }

            if (showSheet.value) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet.value = false },
                    sheetState = bottomSheetState,
                    scrimColor = Color.Transparent
                ) {
                    NewEventDialog(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.3f),
                        onCreateNewContact = onCreateNewContact,
                        onBookMeeting = { launchPermissionRequest?.invoke() },
                        onClear = { showSheet.value = false }
                    )
                }
            }
        }
    }
}

@Composable
fun NewEventDialog(
    modifier: Modifier,
    onCreateNewContact: () -> Unit,
    onBookMeeting: () -> Unit,
    onClear: () -> Unit
) {
    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.height(30.dp))
            Icon(
                imageVector = Icons.Outlined.Clear,
                contentDescription = null,
                tint = blue400,
                modifier = Modifier.align(Alignment.End)
                    .clickable { onClear() }
                    .padding(end = 30.dp)
            )
            Spacer(Modifier.height(20.dp))
            AddNewEvent(
                icon = R.drawable.baseline_calendar_today_24,
                eventText = "Book a meeting",
                onClick = onBookMeeting ,
                modifier = Modifier.padding(top = 15.dp , end = 15.dp , bottom = 15.dp , start = 20.dp)
            )
            Spacer(Modifier.height(20.dp))
            AddNewEvent(
                icon = R.drawable.baseline_person_add_alt_24,
                eventText = "Create new contact",
                onClick = onCreateNewContact ,
                modifier = Modifier.padding(top = 15.dp , end = 15.dp , bottom = 15.dp , start = 20.dp)
            )
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}
