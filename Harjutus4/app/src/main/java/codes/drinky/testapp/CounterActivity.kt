package codes.drinky.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class CounterActivity : AppCompatActivity() {

    private var timesClicked: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        val counterBtn = findViewById<Button>(R.id.counterBtn)
        val counter = findViewById<TextView>(R.id.counter)

        counterBtn.setOnClickListener {
            increment(counter)
        }
    }

    private fun increment(counter: TextView) {
        timesClicked++
        counter.text = timesClicked.toString()
    }
}