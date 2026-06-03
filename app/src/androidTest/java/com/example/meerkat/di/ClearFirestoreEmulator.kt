package com.example.meerkat.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


suspend fun clearFirestoreEmulator(
    host: String,
    port: String
) {
    withContext(Dispatchers.IO) {
        val url =
            URL("http://$host:$port/emulator/v1/projects/meerkat-c104c/databases/(default)/documents")
        (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "DELETE"
            connect()
            inputStream.close()
        }
    }
}
