package codes.drinky.testapp

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class AcceleratorActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var square: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accelerator)

        // Ensure phone is always using light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        square = findViewById(R.id.square)

        setupSensor()
    }

    private fun setupSensor() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val sidesX = event.values[0]
            val sidesY = event.values[1]

            square.apply {
                rotationX = sidesX * 3f
                rotationY = sidesY * 3f
                rotation = -sidesX
                translationX = sidesX * -10
                translationY = sidesY * 10
            }

            val color = if (sidesX.toInt() == 0 && sidesY.toInt() == 0) Color.GREEN else Color.RED
            square.setBackgroundColor(color)
            square.text = "up/down ${sidesY.toInt()}\nleft/right ${sidesX.toInt()}"
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this) // to make sure sensors are not used after closing
        super.onDestroy()
    }
}