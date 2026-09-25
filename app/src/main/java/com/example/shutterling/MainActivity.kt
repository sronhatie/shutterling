package com.example.shutterling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.shutterling.ui.theme.ShutterlingTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShutterlingTheme {
                ShowCaptureButton()
            }
        }
    }
}

@Composable
fun ShowCaptureButton() {
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            sendRequest("startRecMode")
        }
    }

    var response by remember {
        mutableStateOf("Nothing yet")
    }

    val scope = rememberCoroutineScope()

    Button(onClick = {
        scope.launch {
            response = withContext(Dispatchers.IO) {
                sendRequest("actTakePicture")
            }
        }
    }) {
        Text("TAKE PICTURE")
    }

    Text(response)
}
