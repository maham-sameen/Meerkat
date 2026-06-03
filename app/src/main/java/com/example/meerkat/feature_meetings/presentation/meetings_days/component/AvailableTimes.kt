package com.example.meerkat.feature_meetings.presentation.meetings_days.component



import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.R
import com.example.meerkat.feature_meetings.domain.util.TimeSlot
import com.example.meerkat.ui.theme.blue400


@Composable
fun AvailableTimes(
    state : LazyListState,
    timeSlots : SnapshotStateList<TimeSlot>,
    onItemClick: (Int) -> Unit ,
    timeZone : String ,
    onEditTimeZone: () -> Unit
) {
    if(timeSlots.isEmpty()){
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
    else{
        LazyColumn(
            state = state,
            contentPadding = PaddingValues(10.dp)
        ) {
            item {
                EditTimeZone(
                    timeZone = timeZone,
                    onEditTimeZone = onEditTimeZone
                )
            }
            itemsIndexed(
                items = timeSlots.toList(),
                key = { index: Int, item: TimeSlot ->
                    index
                },
            ) { index: Int, item: TimeSlot ->
                if (item.isAvailable) {
                    AvailableTimeItem(item.label, onClick = { onItemClick(index) })
                }
            }
        }
    }
}

@Composable
fun EditTimeZone(
    timeZone : String  = "Pakistan , Maldives Time" ,
    onEditTimeZone : () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Recommended times are determined by yours availability settings" ,
            fontSize = 20.sp  ,
            color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            Text(
                text = timeZone,
                fontSize = 19.sp,
                color = blue400
            )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                painter = painterResource(R.drawable.outline_edit_24) ,
                contentDescription = null ,
                tint = blue400 ,
                modifier = Modifier.clickable{onEditTimeZone()}
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun AvailableTimeItem(time : String , onClick : () -> Unit) {
    Box(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .height(60.dp)
            .border(
                width = 1.dp,
                color = blue400,
                shape = RoundedCornerShape(30.dp)
            )
            .clickable {
                onClick()
            }
        ,
        contentAlignment = Alignment.Center
    ) {
        Text(text = time , color = blue400 , fontSize = 20.sp)
    }
}
