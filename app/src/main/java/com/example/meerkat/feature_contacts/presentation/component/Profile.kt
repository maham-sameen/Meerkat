package com.example.meerkat.feature_contacts.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun NewContactsProfile(
    firstCapitalLetterTxt : @Composable () -> Unit ,
    contactNameTxtField : @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth() ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Center
    ) {
        ContactLogo(
            modifier = Modifier.size(100.dp),
            firstCapitalLetterTxt = firstCapitalLetterTxt
        )
        Spacer(Modifier.height(20.dp))
        contactNameTxtField()
        Spacer(Modifier.height(40.dp))
    }
}

