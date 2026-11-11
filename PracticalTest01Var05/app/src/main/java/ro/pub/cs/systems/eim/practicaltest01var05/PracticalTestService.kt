package ro.pub.cs.systems.eim.practicaltest01var05

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

class PracticalTestService : Service() {

    private var processingThread: ProcessingThread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val str = intent?.getStringExtra("string") ?: "empty...."
        val count = intent?.getIntExtra("count", 0) ?: 0

        processingThread = ProcessingThread(this, str, count)
        processingThread?.start()

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        processingThread?.stopThread()
        super.onDestroy()
    }

}

class ProcessingThread(private val context: Context, private val str: String, private val count: Int) : Thread() {
    private var isRunning = true
    private val actions = listOf(
        "ro.pub.cs.systems.eim.action1"
    )

    override fun run() {
        while (isRunning) {
//            val date = java.util.Date().toString()
//            val arithmeticMean = (num1 + num2) / 2.0
//            val geometricMean = Math.sqrt((num1 * num2).toDouble())

            val intent = Intent()
            intent.action = actions.random()
            intent.putExtra("message", "String = $str")

            for (i in 1..count) {
                context.sendBroadcast(intent)
            }

            try {
                sleep(6000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun stopThread() {
        isRunning = false
    }
}