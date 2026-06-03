package com.example.meerkat.keycloak.auth.presentation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AuthWithKeyCloak(
    viewModel: ViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val authorizationLauncher =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        println("authorizationLauncher : ${result.data!!}")
                        result.data?.let { data ->
                            viewModel.handleAuth(data)
                        }
                    }
                }
            Button(
                onClick = {
                    viewModel.goWithKeyCloak {
                        authorizationLauncher.launch(it)
                    }
                }
            ) {
                Text("Go With Keycloak")
            }
        }
    }
}