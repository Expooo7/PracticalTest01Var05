package ro.pub.cs.systems.eim.practicaltest01var05

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_secondary)

        val totalText: TextView = findViewById(R.id.total_text)
        val verifyButton: Button = findViewById(R.id.verify_button)
        val cancelButton: Button = findViewById(R.id.cancel_button)

        if (intent != null && intent.hasExtra("string")) {
            val str = intent.getStringExtra("string").toString()
//            val str = "test"
            totalText.text = "String: $str"
        }

        verifyButton.setOnClickListener {
            setResult(RESULT_OK, Intent())
            finish()
        }

        cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED, Intent())
            finish()
        }
    }
}