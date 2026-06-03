package com.example.meerkat.feature_contacts.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meerkat.R
import com.example.meerkat.core.time_zone.domain.util.TimeZoneInfo
import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_contacts.domain.model.PhoneNumber
import com.example.meerkat.ui.theme.blue400


@Preview(device = Devices.PIXEL_C, showBackground = true)
@PreviewLightDark
@Composable
fun ContactInfoPreview() {
    ContactInfo(
        contact = ContactEntity(
           contactId = 0,
            userId = "jopdopwqid[",
            name = "Maham",
            email = "mahamsameenhere@gmail.com",
            phoneNumber = PhoneNumber("92" , "3276010123") ,
            timeZoneInfo = TimeZoneInfo("Karachi" , "Asia/Karachi")
        ),
        onBackPressed = {},
        onEditContact = {}
    )
}


@Composable
fun ContactInfo(
    contact: ContactEntity ,
    onBackPressed: () -> Unit , 
    onEditContact: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarContactInfo(
                onBackPressed = onBackPressed,
                onEditContact = onEditContact
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ProfileContactInfo(
                profileText = contact.name.first().uppercase(),
                name = contact.name
            )
            Spacer(modifier = Modifier.height(30.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(20.dp))
            DetailOfContactInfo(
                email = contact.email,
                phoneNumber = contact.phoneNumber.countryCode+contact.phoneNumber.phoneNumber,
                timeZone = contact.timeZoneInfo.name
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarContactInfo(
    onBackPressed : () -> Unit ,
    onEditContact : () -> Unit
) {
    TopAppBar(
        title = {},
        modifier = Modifier.padding(start = 10.dp , end = 10.dp),
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
                    .clickable{
                        onBackPressed()
                    }
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Outlined.Create,
                contentDescription = null,
                modifier = Modifier.size(35.dp)
                    .clickable{
                        onEditContact()
                    }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface ,
            navigationIconContentColor = blue400 ,
            actionIconContentColor = blue400
        ),
    )
}


@Composable
fun DetailOfContactInfo(
    email : String ,
    phoneNumber : String ,
    timeZone : String
) {
    Column (
        modifier = Modifier.padding(start = 20.dp)
    ){
        Text(
            text = "DETAILS" ,
            fontWeight = FontWeight.Bold ,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
        DetailComp(
            iconModifier = Modifier,
            imageVector = Icons.Outlined.Email,
            painter = null,
            field = {
                Text(
                    text = email ,
                    color = blue400
                )
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        DetailComp(
            iconModifier = Modifier,
            imageVector = Icons.Outlined.Phone,
            painter = null,
            field = {
                Text(
                    text = phoneNumber,
                    color = blue400
                )
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        DetailComp(
            iconModifier = Modifier,
            painter = R.drawable.baseline_public_24,
            field = {
                Text(
                    text = timeZone ,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )

    }
}


@Composable
fun ProfileContactInfo(
    profileText : String  ,
    name : String
) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(20.dp))
        ContactLogo(
            modifier = Modifier.size(90.dp),
            firstCapitalLetterTxt = {
                Text(
                    text = profileText ,
                    fontWeight = FontWeight.Bold ,
                    fontSize = 30.sp ,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = name,
            fontWeight = FontWeight.Bold ,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Contact",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            ContactInfoComp(
                painter = R.drawable.baseline_event_24 ,
                text = "Book meeting"
            )
            Spacer(modifier = Modifier.width(20.dp))
            ContactInfoComp(
                imageVector = Icons.Outlined.Email ,
                text = "share availability"
            )
        }
    }
}


@Composable
fun ContactInfoComp(
    imageVector : ImageVector? = null,
    @DrawableRes
    painter : Int? = null,
    text : String
) {
    Card (
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier,
        shape = RoundedCornerShape(10.dp),
    ){
        Column(
            modifier = Modifier.padding(20.dp , 20.dp),
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(imageVector != null){
                Icon(
                    imageVector = imageVector,
                    contentDescription = "",
                    tint = blue400
                )
            }else{
                Icon(
                    painter = painterResource(painter!!),
                    contentDescription = "",
                    tint = blue400
                )
            }
            Text(
                text = text ,
                color = blue400
            )
        }
    }
}