package com.example.shutterling

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CameraScreen() {
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val scope = rememberCoroutineScope()

    // start the camera connection and live-view loop once
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            sendRequest("startRecMode")
        }

        val stream = withContext(Dispatchers.IO) {
            openLiveViewStream()
        }

        while (isActive) {
            val newBitmap = withContext(Dispatchers.IO) {
                val jpegData = readLiveViewFrame(stream)

                // converts JPEG into android bitmap that can be displayed
                BitmapFactory.decodeByteArray(
                    jpegData,
                    0,
                    jpegData.size
                )
            }
            bitmap = newBitmap
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .requiredSize(
                            width = maxHeight,
                            height = maxWidth
                        )
                        .rotate(90f),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Button(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    sendRequest("actTakePicture")
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("TAKE PICTURE")
        }
    }
}
