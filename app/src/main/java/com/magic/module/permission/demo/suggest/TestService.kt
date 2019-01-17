package com.magic.module.permission.demo.suggest

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.magic.module.permission.demo.MainActivity

/**
 * @author zhangyanjiao
 * @data on 2018/12/10 10:23
 * @desc TestService.kt
 */

class TestService : Service() {

    override fun onCreate() {
        super.onCreate()
        startNotify(this)
    }

    private val mBinder = object : IMyAidlInterface.Stub() {
        override fun requestPermission() {
            PermissionUtils.requestPermission(this@TestService, PermissionFuncType.CLEANFUNC, true, object : PermissionCallback() {
                override fun onGranted() {
                    super.onGranted()
                    Log.d("aaa", "onGranted")
                }

                override fun onDenied() {
                    super.onDenied()
                    Log.d("aaa", "onDenied")
                }
            })
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
//        return RecordBinder()
        return mBinder
    }

    private fun startNotify(service: Service) {
        val notification: Notification = NotificationHelper.instance.mainNotification
        service.startForeground(1, notification)
    }

    inner class RecordBinder : Binder() {
        val recordService: TestService
            get() = this@TestService
    }
}
