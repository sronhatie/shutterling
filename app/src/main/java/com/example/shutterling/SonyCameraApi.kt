package com.example.shutterling

import org.json.JSONObject
import java.io.DataInputStream
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

    val output = connection.inputStream.bufferedReader().use {
        it.readText()
    }

    return output
}

fun readLiveViewFrame(stream: DataInputStream): ByteArray {
    /*
    * Common Header is constructed of the following 8 Bytes.
    * Start byte : 1 [B]
    * Payload type : 1 [B]
    * Sequence number : 2 [B]
    * Time stamp : 4 [B]
    * */
    val commonHeader = ByteArray(8)
    stream.readFully(commonHeader)

    /*
    * Payload header format will be as following 128 Bytes.
    * Start code : 4[B]
    * Payload data size without padding size : 3[B]
    * Padding size : 1[B]
    * Remaining 120 bytes depend on the payload type.
    * */
    val payloadHeader = ByteArray(128)
    stream.readFully(payloadHeader)

    val jpegSize = readUInt24BigEndian(payloadHeader, 4)
    val paddingSize = payloadHeader[7].toInt() and 0xFF

    val jpegData = ByteArray(jpegSize)
    stream.readFully(jpegData)

    if (paddingSize > 0) {
        stream.skipBytes(paddingSize)
    }

    return jpegData
}

fun openLiveViewStream(): DataInputStream {
    val response = sendRequest("startLiveview")

    val json = JSONObject(response)
    val liveViewUrl = json.getJSONArray("result").getString(0)

    val connection = URL(liveViewUrl).openConnection() as HttpURLConnection

    connection.requestMethod = "GET"

    return DataInputStream(connection.inputStream)
}

fun readUInt24BigEndian(bytes: ByteArray, offset: Int): Int {
    // read bytes and convert them to unsigned 0-255 values
    val first = bytes[offset].toInt() and 0xFF
    val second = bytes[offset + 1].toInt() and 0xFF
    val third = bytes[offset + 2].toInt() and 0xFF

    // shl = shift left. moves each byte into correct position
    val firstShifted = first shl 16
    val secondShifted = second shl 8

    // combine all three bytes into one integer
    val value = firstShifted or secondShifted or third

    return value
}
