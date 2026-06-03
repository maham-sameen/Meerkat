package com.example.meerkat.feature_contacts.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.core.presentation.component.isDarKMode
import com.example.meerkat.feature_contacts.domain.model.ContactEntity

@Composable
fun Contacts(
    modifier: Modifier ,
    list : List<ContactEntity> ,
    onClick: ( item : ContactEntity) -> Unit
) {
    LazyColumn (
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface) ,
        contentPadding = PaddingValues(top = 10.dp , start = 20.dp , end  = 20.dp , bottom = 10.dp) ,

    ){
        itemsIndexed(
            items = list ,
            key = {
                index : Int , item : ContactEntity ->
                index.hashCode()
            }
        ) { index : Int , item : ContactEntity ->
            ContactsDet(
                name = item.name,
                email = item.email ,
                onClick = { onClick(item) }
            )
        }
    }
}


@Composable
fun ContactsDet(
    name: String ,
    email: String ,
    onClick: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()

    ){
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            verticalAlignment = Alignment.Top ,
            horizontalArrangement = Arrangement.Center
        ){
            ContactLogo(
                modifier = Modifier.size(25.dp),
                firstCapitalLetterTxt = {
                    Text(
                        text = name[0].uppercase(),
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant ,
                        textAlign = TextAlign.Center
                    )
                }
            )
            Spacer(modifier = Modifier.width(4.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.width(20.dp))
        }
        Spacer(Modifier.height(20.dp))
        ContactBox(
            name = name ,
            email = email ,
            onClick  = onClick
        )
    }
}


@Composable
fun ContactBox(
    name : String ,
    email : String ,
    onClick : () -> Unit
) {
    val context = LocalContext.current
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .clickable {
                onClick()
            }
        ,
        shape = RoundedCornerShape(10.dp)
        ,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        border = BorderStroke(
            1.dp ,
            if(isDarKMode(context)) MaterialTheme.colorScheme.onSurface
            else Color.Transparent
        )
    ){
        Row(
            modifier = Modifier.padding(10.dp) , 
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContactLogo(
                modifier = Modifier.size(35.dp),
                firstCapitalLetterTxt = {
                Text(
                        text = name[0].uppercase(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold ,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                }
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = name ,
                    fontWeight = FontWeight.Bold ,
                    color = MaterialTheme.colorScheme.onSurface

                )
                Text(
                    text = email,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


