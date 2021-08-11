package org.sheba.endless

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    init {
        instance = this
    }

    companion object {
        private var instance: MainActivity? = null

        fun applicationContext() : Context {

            return instance!!.applicationContext
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Order Notfier Service"

        if(getServiceState(applicationContext) == ServiceState.STARTED){
            findViewById<Button>(R.id.btnStartService).visibility = Button.GONE;
        }
        else{
            findViewById<Button>(R.id.btnStopService).visibility = Button.GONE;
        }
        findViewById<Button>(R.id.btnStartService).let {
            it.setOnClickListener {
                log("START THE FOREGROUND SERVICE ON DEMAND")
                actionOnService(Actions.START)
                findViewById<Button>(R.id.btnStopService).visibility = Button.VISIBLE;
                findViewById<Button>(R.id.btnStartService).visibility = Button.GONE;
            }
        }

        findViewById<Button>(R.id.btnStopService).let {
            it.setOnClickListener {
                log("STOP THE FOREGROUND SERVICE ON DEMAND")
                actionOnService(Actions.STOP)
                findViewById<Button>(R.id.btnStopService).visibility = Button.GONE;
                findViewById<Button>(R.id.btnStartService).visibility = Button.VISIBLE;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel =
                NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance)
            mChannel.description = Constants.CHANNEL_DESCRIPTION
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mNotificationManager.createNotificationChannel(mChannel)
        }
        actionOnService(Actions.START)

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                // Log and toast
                getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", token).apply()
                Toast.makeText(this@MainActivity, token, Toast.LENGTH_SHORT).show()
                findViewById<EditText>(R.id.textViewToken).setText(MyFirebaseMessagingService.getToken(this));

                PingSender.pingFakeServer();
            })
//        MyNotificationManager.getInstance(this).displayNotification("Greetings", "Hello how are you?");
    }

    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(this, EndlessService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                log("Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
            log("Starting the service in < 26 Mode")
            startService(it)
        }
    }
    fun getAppContext(): Context? {
        return getAppContext();
    }
}
