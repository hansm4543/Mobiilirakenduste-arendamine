package codes.drinky.testapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
        //defineerime erinevad vaated
        setContentView(binding.root)
        emptyTextView = findViewById(R.id.noUploads)
        uploadsView = findViewById(R.id.uploadList)

        //Loeme rakendusse sisse k6ik kasutaja varasemalt yles laetud pildid
        uploads = fileManager.getUploads()
        parseJson()
        //kaamera nupu defineerimine
        findViewById<Button>(R.id.openCamera).setOnClickListener {
            takeImage() }
        //Galleriist pildi laadimise nupu defineerimine
        findViewById<Button>(R.id.openGallery).setOnClickListener {
            loadImageFromGallery.launch("image/*") }

    }

    private val loadImageFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        //kontrollime kas kasutajal on interneti ühendus, et oleks võimalik pilt üles laadida või mitte
        if (isSuccess && checkForInternet(this)) {
            //laeme pildi ylesse
            uploadImageToImgur(imageConversion.uriToBitmap(latestTmpUri!!))
            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Did not upload, check your internet connection.", Toast.LENGTH_SHORT).show()
        }
    }

    private val loadImageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
        //kontrollime kas pilt on valitud ning kas on olemas internetiyhendus et pilti yles laadida
        if (it != null &&checkForInternet(this)) {
            //laeme pildi ylesse
            uploadImageToImgur(imageConversion.uriToBitmap(it))
            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Did not upload, either you forgot to select a file or you have no internet connection.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkForInternet(context: Context): Boolean {
        //interneti yhenduse kontrollimine
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    private fun uploadImageToImgur(image: Bitmap) {
        //pildi konverteerimine ja yleslaadimine Imgurisse
        var shareLink: String
            imageConversion.getBase64Image(image, complete = { base64Image ->
                GlobalScope.launch(Dispatchers.Default) {
                    val jsonObject = ImgurClient().upload(base64Image)
                    shareLink = jsonObject.getJSONObject("data").getString("link")
                    //saadame pildi m2lusse lisamise funktsioonile
                    pushToHistoryAfterUpload(shareLink)

                }
            })
    }

    private suspend fun pushToHistoryAfterUpload(url: String) {
        //lisame pildi kasutaja yleslaetud piltide m2lusse
        withContext(Dispatchers.Main) {
            val capturedUpload = Upload(System.currentTimeMillis(), url)
            uploads.uploads.add(0, capturedUpload)
            parseJson()
            fileManager.writeToFile(Json.encodeToString(uploads))
            copyToClipboard(url)
        }

    }

    private fun takeImage() {
        //loome lyhiajalise informatsiooni pildi jaoks
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                loadImageFromCamera.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        //loome faili ning kustutame temporary faili peale kaamera vaatest v2ljumist.
        val tmpFile = File.createTempFile("tmp_image_file", ".jpg", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(applicationContext, "${BuildConfig.APPLICATION_ID}.fileprovider", tmpFile)
    }

    fun copyToClipboard(text: String) {
        //kopeerime pildi informatsiooni clipboardile
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    private fun doUploadsExist() {
        //m22rame vaate, mida kasutajale kuvatakse
        if (uploads.uploads.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
        } else {
            emptyTextView.visibility = View.GONE
        }
    }

    private fun parseJson() {
        //kontrollime kas on varasemalt pilte yles laetud
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
        //eemaldame pildi
        this.uploads.uploads.removeIf { it.url == url }
        parseJson()
    }

}