package com.example.loginsqllite

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadRequestBody (
    val file : File,
    val contentType : String,
    val callback: UploadCallback

): RequestBody() {
    override fun contentType() = MediaType.parse("$contentType/*")

    override fun contentLength() = file.length()

    override fun writeTo(sink: BufferedSink) {
        val length = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = FileInputStream(file)
        var uploaded = 0L
        fileInputStream.use { inputStream ->
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            while (inputStream.read(buffer).also {
                    read = it
                } != -1) {
                uploaded += read
                sink.write(buffer, 0, read)
            }
        }
    }


    interface UploadCallback {

        fun onProgressUpdate(percentage: Int)


    }

    inner class ProgressUpdater(
        val uploaded: Long,
        val total: Long
    ) : Runnable {
        override fun run() {
            callback.onProgressUpdate((100*uploaded/total).toInt())

        }


    }
    companion object{
        const val DEFAULT_BUFFER_SIZE = 2048
    }
}