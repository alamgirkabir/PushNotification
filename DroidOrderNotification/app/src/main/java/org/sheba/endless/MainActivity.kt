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
import android.content.pm.PackageManager

import android.content.pm.ResolveInfo

import android.content.ComponentName
import android.widget.EditText
import android.widget.TextView
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        title = "Order Notfier Service"

//        val manufacturer = Build.MANUFACTURER
//        try {
//            val intent = Intent()
//            if ("xiaomi".equals(manufacturer, ignoreCase = true)) {
//                intent.component = ComponentName(
//                    "com.miui.securitycenter",
//                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
//                )
//            } else if ("oppo".equals(manufacturer, ignoreCase = true)) {
//                intent.component = ComponentName(
//                    "com.coloros.safecenter",
//                    "com.coloros.safecenter.permission.startup.StartupAppListActivity"
//                )
//            } else if ("vivo".equals(manufacturer, ignoreCase = true)) {
//                intent.component = ComponentName(
//                    "com.vivo.permissionmanager",
//                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
//                )
//            } else if ("Letv".equals(manufacturer, ignoreCase = true)) {
//                intent.component = ComponentName(
//                    "com.letv.android.letvsafe",
//                    "com.letv.android.letvsafe.AutobootManageActivity"
//                )
//            } else if ("Honor".equals(manufacturer, ignoreCase = true)) {
//                intent.component = ComponentName(
//                    "com.huawei.systemmanager",
//                    "com.huawei.systemmanager.optimize.process.ProtectActivity"
//                )
//            }
//            val list =
//                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
//            if (list.size > 0) {
//                startActivity(intent)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

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
        findViewById<EditText>(R.id.textViewToken).setText(MyFirebaseMessagingService.getToken(this));
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
}
