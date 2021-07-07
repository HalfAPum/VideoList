package com.example.videolist.data.localsource.filesystem

import android.os.Environment
import android.util.Log
import com.example.videolist.utils.Constants.TAG
import okhttp3.ResponseBody
import java.io.*
import javax.inject.Inject

//class that operates with filesystem
class FileSystem @Inject constructor() {

    fun writeToDisk(body: ResponseBody, path: File, name: String): Boolean {
        try {
            val video = File(
                path.path + File.separator + name
            )
            if(!video.exists()) {
                video.createNewFile()
            }
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(video)
                while (true) {
                    val read = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                }
                outputStream.flush()
                return true
            } catch (e: IOException) {
                return false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            return false
        }
    }
}