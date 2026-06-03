package com.example.meerkat.feature_meetings.presentation.meetings_days

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.meerkat.core.presentation.SessionExpiredDialog
import com.example.meerkat.core.time_zone.presentation.TimeZoneBottomSheet
import com.example.meerkat.core.presentation.SearchEntriesTxtField
import com.example.meerkat.feature_meetings.domain.util.MeetingDate
import com.example.meerkat.feature_meetings.domain.util.TimeSlot
import com.example.meerkat.feature_meetings.presentation.meetings_days.component.Aday
import com.example.meerkat.feature_meetings.presentation.meetings_days.component.AvailableTimes
import com.example.meerkat.feature_meetings.presentation.meetings_days.component.CustomizeOptions
import com.example.meerkat.feature_meetings.presentation.meetings_days.component.DatePicker
import com.example.meerkat.feature_meetings.presentation.meetings_days.component.TopMeetingDaysBar
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(device = Devices.PIXEL_C, showBackground = true)
@PreviewLightDark
@Composable
fun MeetingDaysPreview() {
    val viewModel = hiltViewModel<MDViewModel>()
    MeetingDays(null,viewModel , {}) { timeSlot, eventId ->  }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingDays(
    eventId : Long? = null ,
    viewModel : MDViewModel,
    onLogInAgain : () ->  Unit ,
    addGuest : (timeSlot : TimeSlot , eventId : Long?) -> Unit
) {
    var weekOfDay  = viewModel.currentWeek
    val dateDialogState = rememberMaterialDialogState()
    val daysListState = rememberLazyListState()
    val timeSlotListState = rememberLazyListState()



    val isSearching = viewModel.isSearching.collectAsStateWithLifecycle()
    val timeZones = viewModel.timeZonesCategory.collectAsStateWithLifecycle()
    val selectedOption  = viewModel.selectedTimeZone
    val searchText = viewModel.searchText.collectAsStateWithLifecycle()
    val currentTimeZone = viewModel.currentTimeZone

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState =  rememberStandardBottomSheetState(
            skipHiddenState = false,
        )
    )

    val customizeEventState = rememberModalBottomSheetState()
    val showCustomizeEvent = viewModel.showCustomizeEvent
    val host by viewModel.host.collectAsState()


    LaunchedEffect(viewModel.selectedOption.value) {
        daysListState.animateScrollToItem(viewModel.currentSelectedDateIndex.value)
        timeSlotListState.animateScrollToItem(0)
    }

    LaunchedEffect(Unit) {
        viewModel.toUpdateMeetings(eventId)
    }

    BottomSheetScaffold(
        sheetContent = {
            TimeZoneBottomSheet(
                isSearching = isSearching.value,
                timeZones = timeZones.value,
                selectedOption = selectedOption.value,
                onToggleSelection = { viewModel.onEvent(MeetingsDaysEvents.OnToggleSelection(it)) },
                searchField = {
                    SearchEntriesTxtField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp)
                        ,
                        hint = "Search",
                        text = searchText.value,
                        onTextChange = { viewModel.onEvent(MeetingsDaysEvents.OnSearchTxt(it)) } ,
                        onClear = {viewModel.onEvent(MeetingsDaysEvents.OnSearchTxt(""))} ,
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
                        viewModel.onEvent(MeetingsDaysEvents.OnDone)
                        scaffoldState.bottomSheetState.hide()
                    }
                },
            )
        } ,
        topBar = {
            Column {
                TopMeetingDaysBar(
                    startWeek = weekOfDay.value.monday.monthName.take(3) + " ${weekOfDay.value.monday.dayNumber}",
                    endWeek = weekOfDay.value.sunday.monthName.take(3) + " ${weekOfDay.value.sunday.dayNumber}",
                    year = weekOfDay.value.sunday.yearNumber,
                    openCalendar = {
                        dateDialogState.show()
                    },
                )
                HorizontalDivider()
            }
        } ,
        modifier = Modifier,
        scaffoldState = scaffoldState,
        sheetMaxWidth = BottomSheetDefaults.SheetMaxWidth,
        sheetContainerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding) ,
        ) {
            Box{
                if(viewModel.sessionDialogState.value){
                    SessionExpiredDialog(
                        onDismiss = { viewModel.onEvent(MeetingsDaysEvents.DismissDialog) },
                        onLoginAgain = onLogInAgain
                    )
                }
                Column {
                    Spacer(Modifier.height(10.dp))
                    LazyRow (
                        state = daysListState
                    ){
                        itemsIndexed(
                            items = viewModel.days,
                            key = { index: Int, day: MeetingDate ->
                                day.hashCode()
                            }
                        ) { index: Int, day: MeetingDate ->
                            day.dayName.take(1)
                            Aday(
                                meetingDate = day,
                                selectedOption = viewModel.selectedOption.value,
                                onDaySelection = { viewModel.onEvent(MeetingsDaysEvents.SelectADay(index)) }
                            )
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    HorizontalDivider()
                    val currentDate = viewModel.selectedOption
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "${currentDate.value.dayName} , ${currentDate.value.monthName} ${currentDate.value.dayNumber}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider()
                    AvailableTimes(
                        state = timeSlotListState,
                        timeSlots = viewModel.currentTimeSlots,
                        onItemClick = {
                            viewModel.onEvent(MeetingsDaysEvents.SelectTimeSlot(it))
                        },
                        timeZone = currentTimeZone.value.name,
                        onEditTimeZone = {
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        }
                    )
                }
                DatePicker(
                    dateDialogState = dateDialogState,
                    onSelectDate = { viewModel.onEvent(MeetingsDaysEvents.SelectNewDate(it)) }
                )
                if(showCustomizeEvent.value){
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.onEvent(MeetingsDaysEvents.DismissCustomizeEvent)},
                        sheetState = customizeEventState
                    ) {
                        CustomizeOptions(
                            modifier = Modifier,
                            host = host ,
                            addGuest = { addGuest(viewModel.selectedTimeSots.value , eventId) }
                        )
                    }
                }
            }
        }
    }
}





