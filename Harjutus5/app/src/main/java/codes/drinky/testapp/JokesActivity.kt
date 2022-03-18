package codes.drinky.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import codes.drinky.testapp.model.Joke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class JokesActivity : AppCompatActivity() {

    private lateinit var jokeTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var jokeBtn: Button
    private var joke: Joke? = null
    private var apiUrl = "https://v2.jokeapi.dev/joke/Any?type=single"
    private val json = Json { ignoreUnknownKeys = true }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jokes)
        setComponents()
        setListeners()
        changeJoke()
    }

    private fun setComponents() {
        jokeBtn = findViewById(R.id.fetchJokeBtn)
        jokeTextView = findViewById(R.id.joke)
        categoryTextView = findViewById(R.id.jokeCategory)
    }

    private fun setListeners() {
        jokeBtn.setOnClickListener { GlobalScope.launch (Dispatchers.IO) {
            changeJoke()
        } }
    }

    private fun changeJoke() {
        lifecycleScope.launch(Dispatchers.IO) {
            joke = fetchJoke()
            jokeTextView.text = joke?.joke ?: "Could not fetch joke"
            categoryTextView.text = joke?.category ?: "Error"
        }
    }

    private fun fetchJoke(): Joke? {
        return fetch(apiUrl)
    }

    private fun httpGet(urlAddress: String?): String? {
        val inputStream: InputStream
        var result: String? = null

        try {
            val url = URL(urlAddress)
            val conn: HttpsURLConnection = url.openConnection() as HttpsURLConnection
            conn.connect()
            inputStream = conn.inputStream

            result = inputStream?.bufferedReader()?.use(BufferedReader::readText) ?: "error: inputStream is null"
        } catch (err: Error) {
            print("Error when executing request: ${err.localizedMessage}")
        }
        return result
    }


    private fun fetch(url: String): Joke? {
        var jokeInfo: String? = null
        jokeInfo = httpGet(url)
        println(jokeInfo)
        return json.decodeFromString<Joke>(jokeInfo as String)
    }
}