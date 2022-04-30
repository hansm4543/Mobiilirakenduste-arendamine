package codes.drinky.testapp

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import codes.drinky.testapp.client.ImgurClient
import codes.drinky.testapp.databinding.ActivityMainBinding
import codes.drinky.testapp.helpers.ImageConversion
import codes.drinky.testapp.manager.UploadsFileManager
import codes.drinky.testapp.model.Upload
import codes.drinky.testapp.model.Uploads
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONException
import java.io.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var uploads: Uploads
    private lateinit var emptyTextView: TextView
    private lateinit var uploadsView: RecyclerView

    private val fileManager = UploadsFileManager(this)
    private val imageConversion = ImageConversion(this)
    private var latestTmpUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        emptyTextView = findViewById(R.id.noUploads)
        uploadsView = findViewById(R.id.uploadList)

        uploads = fileManager.getUploads()
        parseJson()

        findViewById<Button>(R.id.openCamera).setOnClickListener {
            takeImage() }

        findViewById<Button>(R.id.openGallery).setOnClickListener {
            loadImageFromGallery.launch("image/*") }

    }

    private val loadImageFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            uploadImageToImgur(imageConversion.uriToBitmap(latestTmpUri!!))
            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Did not upload", Toast.LENGTH_SHORT).show()
        }
    }

    private val loadImageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            uploadImageToImgur(imageConversion.uriToBitmap(it))
            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Did not upload", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToImgur(image: Bitmap) {
        var shareLink: String
        imageConversion.getBase64Image(image, complete = { base64Image ->
            GlobalScope.launch(Dispatchers.Default) {
                val jsonObject = ImgurClient().upload(base64Image)
                shareLink = jsonObject.getJSONObject("data").getString("link")
                pushToHistoryAfterUpload(shareLink)

            }
        })
    }

    private suspend fun pushToHistoryAfterUpload(url: String) {
        withContext(Dispatchers.Main) {
            val capturedUpload = Upload(System.currentTimeMillis(), url)
            uploads.uploads.add(0, capturedUpload)
            parseJson()
            fileManager.writeToFile(Json.encodeToString(uploads))
            copyToClipboard(url)
        }

    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                loadImageFromCamera.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".jpg", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(applicationContext, "${BuildConfig.APPLICATION_ID}.fileprovider", tmpFile)
    }

    fun copyToClipboard(text: String) {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    private fun doUploadsExist() {
        if (uploads.uploads.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
        } else {
            emptyTextView.visibility = View.GONE
        }
    }

    private fun parseJson() {
        doUploadsExist()
        try {
            uploadsView.layoutManager = LinearLayoutManager(this)
            val itemAdapter = UploadAdapter(this, uploads.uploads)
            uploadsView.adapter = itemAdapter
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun remove(url: String) {
        this.uploads.uploads.removeIf { it.url == url }
        parseJson()
    }

}