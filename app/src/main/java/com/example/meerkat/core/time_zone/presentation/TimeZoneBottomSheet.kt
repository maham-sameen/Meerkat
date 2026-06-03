package com.example.meerkat.core.time_zone.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.core.time_zone.domain.util.TimeZoneCategory
import com.example.meerkat.core.time_zone.domain.util.TimeZoneInfo
import com.example.meerkat.ui.theme.blue400

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeZoneBottomSheet(
    isSearching : Boolean,
    timeZones : List<TimeZoneCategory>,
    searchField : @Composable () -> Unit,
    selectedOption : TimeZoneInfo,
    onToggleSelection: (TimeZoneInfo) -> Unit,
    onClose: () -> Unit,
    onDone: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        TopBottomSheetBar(
            onClose = onClose,
            onDone = onDone
        )
        HorizontalDivider()
        searchField()
        Spacer(modifier = Modifier.height(16.dp))
        if (isSearching) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }else if (timeZones.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "No result found",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }  else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .selectableGroup()
                ,
                contentPadding = PaddingValues(vertical = 10.dp)
            ){
                timeZones.forEach{ timeZoneCategory ->
                    stickyHeader (
                        key = timeZoneCategory.hashCode()
                    ){
                        TimeZoneHeading(
                            text = timeZoneCategory.name.name
                        )
                    }
                    itemsIndexed(
                        items = timeZoneCategory.items,
                        key = { index: Int, timeZoneInfo: TimeZoneInfo -> timeZoneInfo.hashCode()},
                        itemContent = { index: Int, timeZoneInfo: TimeZoneInfo ->
                            TimeZonesItem(
                                timeZoneInfo = timeZoneInfo,
                                selectedOption = selectedOption,
                                onClick = { onToggleSelection(timeZoneInfo) }
                            )
                        }
                    )
                }
                if(timeZones.isEmpty()){
                    item {
                        Text(
                            text = "No result found" ,
                            fontSize = 14.sp ,
                            fontWeight = FontWeight.SemiBold ,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
            }
        }
    }
}







@Composable
fun TopBottomSheetBar(
    onClose : () -> Unit ,
    onDone : () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
        ,
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.Center
    ) {
        BottomSheetTextButton(text = "Close"  , onClick = onClose)
        Text(
            text = "Edit time zone" ,
            fontSize = 18.sp ,
            fontWeight = FontWeight.SemiBold ,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.weight(1f) ,
            textAlign = TextAlign.Center
        )
        BottomSheetTextButton(text = "Done" ,  onClick = onDone )
    }
}


@Composable
fun BottomSheetTextButton(text : String , onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent ,
            contentColor = blue400
        ),
        content = {
            Text(
                text = text ,
                fontSize = 18.sp
            )
        }
    )
}


@Composable
fun TimeZoneHeading( text : String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.CenterStart
    ){
        Text(
            text = text,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
    }
}
