package com.example.xiamibadge

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import me.leolin.shortcutbadger.ShortcutBadger

class MainActivity : FlutterActivity() {

    private lateinit var notificationManager: NotificationManager

    private var notificationId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "setCounter" -> {
                    val counter = call.argument<Int>("counter") ?: 0
                    showNotification(counter)
                }
                else -> throw Exception("Unknown method")
            }
        }
    }

    private fun showNotification(counter: Int) {
        notificationManager.cancel(notificationId)
        notificationId++

        val builder: Notification.Builder = Notification.Builder(applicationContext)
                .setContentTitle("Title")
//                .setNumber(counter)
                .setContentText("Description")
                .setSmallIcon(R.drawable.ic_launcher)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannel()
            builder.setChannelId(NOTIFICATION_CHANNEL)
        }

        val notification: Notification = builder.build()
        val result = ShortcutBadger.applyCount(applicationContext, counter);
        ShortcutBadger.applyNotification(applicationContext, notification, counter)
        notificationManager.notify(notificationId, notification)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun setupNotificationChannel() {
        val channel = NotificationChannel(NOTIFICATION_CHANNEL, "ShortcutBadger Sample",
                NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL = "com.listta.goals/counter"
        private const val NOTIFICATION_CHANNEL = "me.leolin.shortcutbadger.example"
    }
}
