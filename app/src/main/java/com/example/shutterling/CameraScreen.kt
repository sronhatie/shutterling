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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
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
    // creates a Bitmap that can be null
    // once it updates, changes the UI
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    // use this to launch and manage coroutines
    val scope = rememberCoroutineScope()

    // start the camera connection and live-view loop once
    LaunchedEffect(Unit) {
        // switches the coroutine to I/O dispatcher
        withContext(Dispatchers.IO) {
            sendRequest("startRecMode")
        }

        // saves the DataInputStream object
        val stream = withContext(Dispatchers.IO) {
            openLiveViewStream()
        }

        // continuously reads images from the camera and updates the live preview
        // isActive checks if the coroutine is still active
        while (isActive) {
            val newBitmap = withContext(Dispatchers.IO) {
                // returns a ByteArray
                val jpegData = readLiveViewFrame(stream)

                // converts JPEG ByteArray into android bitmap that can be displayed
                BitmapFactory.decodeByteArray(
                    jpegData,
                    0,
                    jpegData.size
                )
            }
            bitmap = newBitmap
        }
    }

    // use column so the elements stack vertically instead of overlapping
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
            // only runs this if bitmap isn't null
            bitmap?.let {
                Image(
                    // it is the not null bitmap
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .requiredSize(
                            // swap the width and height so it rotates by 90 degrees
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
                // make a new coroutine and send the request in I/O thread
                scope.launch(Dispatchers.IO) {
                    sendRequest("actTakePicture")
                }
            },
            modifier = Modifier
                .padding(bottom = 50.dp)
                .size(150.dp),
            shape = CircleShape
        ) {}
    }
}


