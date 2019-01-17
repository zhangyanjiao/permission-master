package com.magic.module.permission.keep

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.magic.module.permission.utils.RomUtils


/**
 * @author zhangyanjiao
 * @data on 2018/9/4 16:24
 * @desc 不同手机跳转悬浮窗设置界面
 */
/** @deprecated  使用 PermissionPager 完成权限跳转*/
@Deprecated("")
object PermissionAlertPage {
    private val MARK = Build.MANUFACTURER.toLowerCase()

    /**
     * Start.
     *
     * @param requestCode this code will be returned in onActivityResult() when the activity exits.
     * @return true if successful, otherwise is false.
     */
    fun getIntent(context: Context): Intent {
        val targetVersion = context.applicationInfo.targetSdkVersion
        return if (Build.VERSION.SDK_INT >= 23 && targetVersion >= 23) {
            defaultApi(context)
        } else {
            when {
                MARK.contains("huawei") -> huaweiApi(context)
                MARK.contains("xiaomi") -> xiaomiApi(context)
                MARK.contains("oppo") -> oppoApi(context)
                MARK.contains("meizu") -> meizuApi(context)
                else -> defaultApi(context)
            }
        }

    }

    private fun defaultApi(context: Context): Intent {
        val targetVersion = context.applicationInfo.targetSdkVersion
        var intent: Intent
        if (Build.VERSION.SDK_INT >= 23 && targetVersion >= 23) {
            intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.packageName))
        } else {
            intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", context.packageName, null)
        }
        return intent
    }

    private fun huaweiApi(context: Context): Intent {
        val intent = Intent()
        var comp = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity")//悬浮窗管理页面
        if (Build.VERSION.SDK_INT == 21) {
            comp = ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity")//悬浮窗管理页面
        }
        intent.component = comp
        return intent
    }

    private fun xiaomiApi(context: Context): Intent {
        var intent = Intent()
        when (RomUtils.getMiuiVersion()) {
            5 -> {
                val packageName = context.packageName
                intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
            }
            6, 7 -> {
                intent = Intent("miui.intent.action.APP_PERM_EDITOR")
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity")
                intent.putExtra("extra_pkgname", context.packageName)
            }
            8, 9 -> {
                intent = Intent("miui.intent.action.APP_PERM_EDITOR")
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
                intent.putExtra("extra_pkgname", context.packageName)
            }

        }
        return intent
    }

    private fun oppoApi(context: Context): Intent {
        val intent = Intent()
        val comp = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity")//悬浮窗管理页面
        intent.component = comp
        return intent
    }

    private fun meizuApi(context: Context): Intent {
        val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
        intent.putExtra("packageName", context.packageName)
        return intent
    }

    private fun hasActivity(context: Context, intent: Intent): Boolean {
        val packageManager = context.packageManager
        return packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size > 0
    }
}