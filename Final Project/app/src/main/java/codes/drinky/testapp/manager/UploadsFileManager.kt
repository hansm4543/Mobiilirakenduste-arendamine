package codes.drinky.testapp.manager

import android.content.Context
import codes.drinky.testapp.model.Uploads
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream

class UploadsFileManager(private val context: Context) {

    private val fileName = "uploads.json"

    private fun readFile(): String? {
        return try {
            val file = File(context.filesDir, fileName)
            val contents = file.readText()
            contents
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun writeToFile(content: String) {
        val path = context.filesDir
        val file = File(path, fileName)
        file.createNewFile()
        FileOutputStream(file).use {
            it.write(content.toByteArray())
        }
    }

    fun getUploads(): Uploads {
        val uploadsJson = readFile()
        return if (uploadsJson != null) {
            Json.decodeFromString(uploadsJson)
        } else {
            Uploads(arrayListOf())
        }
    }
}