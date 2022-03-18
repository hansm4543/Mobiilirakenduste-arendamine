package codes.drinky.testapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var openCounter: Button
    private lateinit var openJokes: Button
    private lateinit var openAccel: Button
    private lateinit var openCamera: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openCounter = findViewById<Button>(R.id.openCounter)
        openJokes = findViewById<Button>(R.id.openJokes)
        openAccel = findViewById<Button>(R.id.openAccelerometer)
        openCamera = findViewById<Button>(R.id.openCamera)
        setListeners()
    }

    private fun setListeners() {
        openCounter.setOnClickListener { openCounterActivity() }
        openJokes.setOnClickListener { openJokesActivity() }
        openAccel.setOnClickListener { openAcceleratorActivity() }
        openCamera.setOnClickListener { openCameraActivity() }

    }

    private fun openCounterActivity() {
        val intent = Intent(this, CounterActivity::class.java)
        startActivity(intent)
    }

    private fun openAcceleratorActivity() {
        val intent = Intent(this, AcceleratorActivity::class.java)
        startActivity(intent)
    }

    private fun openJokesActivity() {
        val intent = Intent(this, JokesActivity::class.java)
        startActivity(intent)
    }

    private fun openCameraActivity() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }


}