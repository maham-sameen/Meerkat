package com.example.meerkat.feature_meetings.presentation.meetings_days.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.R
import com.example.meerkat.core.presentation.topAppBarColors
import com.example.meerkat.ui.theme.blue400


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMeetingDaysBar(
    startWeek : String ,
    endWeek : String ,
    year : Int ,
    openCalendar : () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Start ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    //  text = "Mar 10 - Mar 16 2025",
                    text = "$startWeek - $endWeek $year",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_drop_down_24),
                    contentDescription = null ,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable(
                            onClick = openCalendar
                        )
                    ,
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        actions = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(R.drawable.baseline_meeting_room_24),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = blue400
                )
            }
        },
        colors = topAppBarColors()
    )
}
