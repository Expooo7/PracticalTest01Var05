package ro.pub.cs.systems.eim.practicaltest01var05

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01Var05MainActivity : AppCompatActivity() {
    private lateinit var textBox: EditText

    private var tlcount = 0
    private var trcount = 0
    private var rightCount = 0
    private var pressCount = 0
    private var stringText = ""

    private val SERVICE_THRESHOLD = 3
    private var serviceStarted = false

    private val messageBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("PracticalTest01Broadcast", intent?.getStringExtra("message") ?: "Empty")
        }
    }
    private fun checkAndStartService() {
        if (pressCount > SERVICE_THRESHOLD && !serviceStarted) {
            val intent = Intent(this, PracticalTestService::class.java)
            intent.putExtra("string", stringText)
            intent.putExtra("count", pressCount)
            startService(intent)
            serviceStarted = true
        }
    }

    private val intentFilter = IntentFilter().apply {
        addAction("ro.pub.cs.systems.eim.action1")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState != null) {
            pressCount = savedInstanceState.getInt("pressCount")
            Toast.makeText(this, "Valoare pressCount = $pressCount", Toast.LENGTH_LONG).show()
        }

        val tlButton: Button = findViewById(R.id.top_left_button)
        val trButton: Button = findViewById(R.id.top_right_button)

        val midButton: Button = findViewById(R.id.middle_button)

        val blButton: Button = findViewById(R.id.bot_left_button)
        val brButton: Button = findViewById(R.id.bot_right_button)
        val navigateButton: Button = findViewById(R.id.navigate_button)

        textBox = findViewById(R.id.text_input)

        tlButton.setOnClickListener {
            stringText = "$stringText, Top Left"
            textBox.setText(stringText)
            pressCount++
            checkAndStartService()
        }

        trButton.setOnClickListener {
            stringText = "$stringText, Top Right"
            textBox.setText(stringText)
            pressCount++
            checkAndStartService()
        }

        midButton.setOnClickListener {
            stringText = "$stringText, Center"
            textBox.setText(stringText)
            pressCount++
            checkAndStartService()
        }

        blButton.setOnClickListener {
            stringText = "$stringText, Bot Left"
            textBox.setText(stringText)
            pressCount++
            checkAndStartService()
        }

        brButton.setOnClickListener {
            stringText = "$stringText, Bot Right"
            textBox.setText(stringText)
            pressCount++
            checkAndStartService()
        }

        navigateButton.setOnClickListener {
            val intent = Intent(this, SecondaryActivity::class.java)
            intent.putExtra("string", stringText)
            activityResultLauncher.launch(intent)
        }

    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, "Result from secondary activity: OK", Toast.LENGTH_LONG).show()
        } else if (result.resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Result from secondary activity: CANCELED", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("pressCount", pressCount)
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageBroadcastReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(messageBroadcastReceiver, intentFilter)
        }
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }
}