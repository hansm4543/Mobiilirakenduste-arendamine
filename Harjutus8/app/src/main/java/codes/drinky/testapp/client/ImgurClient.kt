package codes.drinky.testapp.client

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import codes.drinky.testapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.io.InputStream
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ImgurClient {
    private val CLIENT_ID = BuildConfig.IMGUR_CLIENT_ID
    private val UPLOAD_URL = "https://api.imgur.com/3/image"

    suspend fun upload(base64Image: String): JSONObject {
        val url = URL(UPLOAD_URL)
        val boundary = "Boundary-${System.currentTimeMillis()}"

        val httpsURLConnection =
            withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
        httpsURLConnection.setRequestProperty("Authorization", "Client-ID $CLIENT_ID")
        httpsURLConnection.setRequestProperty(
            "Content-Type",
            "multipart/form-data; boundary=$boundary"
        )

        httpsURLConnection.requestMethod = "POST"
        httpsURLConnection.doInput = true
        httpsURLConnection.doOutput = true

        var body = ""
        body += "--$boundary\r\n"
        body += "Content-Disposition:form-data; name=\"image\""
        body += "\r\n\r\n$base64Image\r\n"
        body += "--$boundary--\r\n"

        val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
        withContext(Dispatchers.IO) {
            outputStreamWriter.write(body)
            outputStreamWriter.flush()
        }

        val response = httpsURLConnection.inputStream.bufferedReader()
            .use { it.readText() }
        return JSONTokener(response).nextValue() as JSONObject
    }

    fun fetchImage(imgUrl: String): Bitmap? {
        return try {
            val url = URL(imgUrl)
            BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}