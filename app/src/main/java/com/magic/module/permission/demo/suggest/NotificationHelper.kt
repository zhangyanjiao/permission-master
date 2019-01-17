package com.magic.module.permission.demo.suggest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import com.magic.module.permission.demo.MainActivity
import com.magic.module.permission.demo.MyApplication
import com.magic.module.permission.demo.R

class NotificationHelper(private val context: Context) : ContextWrapper(context) {

    private var mManager: NotificationManager? = null
    /**
     * 建立一级通道的通知
     */
    private val notification: Notification.Builder
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(applicationContext, PRIMARY_CHANNEL)
                    .setSmallIcon(smallIcon)
                    .setAutoCancel(true)
        } else {
            Notification.Builder(applicationContext).apply {
                setPriority(Notification.PRIORITY_HIGH)
                setWhen(System.currentTimeMillis())
                setSmallIcon(smallIcon)
                setAutoCancel(true)
            }

        }


    val mainNotification: Notification
        get() {
            val notification = notification.build()
            val remoteView = RemoteViews(context.packageName, R.layout.notification_main)
            notification.contentView = remoteView
            notification.contentIntent = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
            return notification
        }

    /**
     * 获取这个应用程序的小图标
     */
    private val smallIcon: Int
        get() = R.mipmap.ic_launcher

    /**
     * 通知管理
     */
    private val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan1 = NotificationChannel(
                    PRIMARY_CHANNEL,
                    "aaa",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            chan1.lightColor = Color.GREEN
            chan1.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            manager?.createNotificationChannel(chan1)
        }
    }

    /**
     * 发送通知
     */
    fun notify(id: Int, notification: Notification.Builder) {
        manager?.notify(id, notification.build())
    }

    fun notify(id: Int, notification: Notification) {
        manager?.notify(id, notification)
    }

    fun removeNotification(id: Int) {
        manager?.cancel(id)
    }

    companion object {
        const val PRIMARY_CHANNEL = "testId"
        const val ACTION_REQUEST_PERMISSION = "action_request_permission"
        val instance: NotificationHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            NotificationHelper(MyApplication.getContext())
        }
    }
}