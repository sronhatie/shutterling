package com.example.shutterling

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CameraScreen() {
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        bitmap = withContext(Dispatchers.IO) {
            sendRequest("startRecMode")

            val jpegData = getLiveViewFrame()

            BitmapFactory.decodeByteArray(
                jpegData,
                0,
                jpegData.size
            )
        }
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null
        )
    }

    Button(
        onClick = {
            scope.launch(Dispatchers.IO) {
                sendRequest("actTakePicture")
            }
        }
    ) {
        Text("TAKE PICTURE")
    }
}