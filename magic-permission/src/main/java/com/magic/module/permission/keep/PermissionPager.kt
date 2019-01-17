package com.magic.module.permission.keep

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings


/**
 * @author zhangyanjiao
 * @data on 2018/6/27 14:41
 * @desc 跳转
 */
object PermissionPager {
    fun getIntent(context: Context): Intent {
        return PermissionSettingPage.getIntent(context)
    }

    fun getIntent(context: Context, permission: List<String>): Intent {
        if (permission != null && permission.isNotEmpty()) {
            return when (permission[0]) {
                Manifest.permission.SYSTEM_ALERT_WINDOW -> PermissionAlertPage.getIntent(context)
                Manifest.permission.ACCESS_NOTIFICATION_POLICY -> getNotificationPolocy(context)
                Manifest.permission.PACKAGE_USAGE_STATS -> getUsageStatePager(context)
                Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE -> getNotificationListenerServicePager(context)
                Manifest.permission.WRITE_SETTINGS -> getWritingSetting(context);
                else -> PermissionSettingPage.getIntent(context)
            }
        }
        return Intent()
    }

    private fun getWritingSetting(context: Context): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + context.packageName)
            intent
        } else {
            PermissionSettingPage.getIntent(context)
        }
    }

    private fun getNotificationListenerServicePager(context: Context): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        } else {
            PermissionSettingPage.getIntent(context)
        }

    }


    fun getIntent(context: Context, permission: String): Intent {
        return when (permission) {
            Manifest.permission.SYSTEM_ALERT_WINDOW -> PermissionAlertPage.getIntent(context)
            Manifest.permission.PACKAGE_USAGE_STATS -> getUsageStatePager(context)
            else -> PermissionSettingPage.getIntent(context)
        }
    }

    /**
     * 跳转记录访问权限
     */
    private fun getUsageStatePager(context: Context): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val intent = Intent()
            intent.action = "android.settings.USAGE_ACCESS_SETTINGS"
            return intent
        } else {
            PermissionSettingPage.getIntent(context)
        }
    }

    private fun getNotificationPolocy(context: Context): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        } else {
            PermissionSettingPage.getIntent(context)
        }
    }


    fun isNoOption(content: Context, intent: Intent): Boolean {
        val packageManager = content.packageManager
        val list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY)
        return list.size > 0
    }
}