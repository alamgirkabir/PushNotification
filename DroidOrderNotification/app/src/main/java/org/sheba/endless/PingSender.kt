package org.sheba.endless

import android.app.Service
import android.provider.Settings
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import java.text.SimpleDateFormat
import java.util.*

class PingSender{
    companion object{

fun pingFakeServer() {
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmmZ")
    val gmtTime = df.format(Date())


    val deviceId = Settings.Secure.getString(MainActivity.applicationContext().contentResolver, Settings.Secure.ANDROID_ID)

    var deviceToken = MainActivity.applicationContext().getSharedPreferences("_", Service.MODE_PRIVATE).getString("fb", null);
    val json =
        """
                {
                    "deviceId": "$deviceId",
                    "createdAt": "$gmtTime",
                    "deviceToken": "$deviceToken"
                }
            """
    try {
        Fuel.post("http://dev-sheba.xyz:8080/devices/add")
            .jsonBody(json)
            .response { _, _, result ->
                val (bytes, error) = result
                if (bytes != null) {
                    log("[response bytes] ${String(bytes)}")
                } else {
                    log("[response error] ${error?.message}")
                }
            }
    } catch (e: Exception) {
        log("Error making the request: ${e.message}")
    }

}
}
}