package codes.drinky.testapp.helpers

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*

class ImageConversion(private val context: Context) {

    fun getBase64Image(image: Bitmap, complete: (String) -> Unit) {
        GlobalScope.launch {
            val outputStream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val b = outputStream.toByteArray()
            complete(Base64.getEncoder().encodeToString(b))
        }
    }

    fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(uri.toString()))
    }
}