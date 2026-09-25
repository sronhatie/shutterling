package com.example.shutterling

import java.net.HttpURLConnection
import java.net.URL

fun sendRequest(method: String): String {
    val url = URL("http://192.168.122.1:8080/sony/camera")

    val connection = url.openConnection() as HttpURLConnection

    connection.requestMethod = "POST"
    connection.setRequestProperty("Content-Type", "application/json")
    connection.doOutput = true

    val body = """{"method":"$method","params":[],"id":1,"version":"1.0"}"""

    connection.outputStream.use {
        it.write(body.toByteArray())
    }

    val output = connection.inputStream.bufferedReader().use { it.readText() }
    return output
}
