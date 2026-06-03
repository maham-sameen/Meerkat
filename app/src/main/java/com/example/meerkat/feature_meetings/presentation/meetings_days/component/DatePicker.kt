package com.example.meerkat.feature_meetings.presentation.meetings_days.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.meerkat.ui.theme.blue400
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(dateDialogState :MaterialDialogState  , onSelectDate : (LocalDate) -> Unit ) {
    //https://github.com/philipplackner/ComposeDateTimePicker/blob/master/app/src/main/java/com/example/composedatetimepicker/MainActivity.kt
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(
                text = "Ok",
                textStyle = TextStyle(
                    color = blue400,
                    fontWeight = FontWeight.SemiBold
                )
            ) {

            }
            negativeButton(
                text = "Cancel" ,textStyle = TextStyle(
                    color = blue400,
                fontWeight = FontWeight.SemiBold
            )){
                
            }
        },
        shape = RoundedCornerShape(10.dp)
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            colors  = DatePickerDefaults.colors(
                headerBackgroundColor = blue400,
                headerTextColor = Color.White,
                calendarHeaderTextColor = blue400,
                dateActiveBackgroundColor = blue400 ,
                dateInactiveBackgroundColor = MaterialTheme.colorScheme.surface,
                dateActiveTextColor = Color.White ,
                dateInactiveTextColor = MaterialTheme.colorScheme.onSurface
            ),
            onDateChange = onSelectDate
        )
    }
}


