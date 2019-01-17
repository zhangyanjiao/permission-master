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
 * @data on 2018/6/27 14:41
 * @desc 不同机型跳转setting界面
 */

/** @deprecated  使用 PermissionPager 完成权限跳转*/
@Deprecated("")
object PermissionSettingPage {
    private val MARK = Build.MANUFACTURER.toLowerCase()

    /**
     * Start.
     *
     * @param requestCode this code will be returned in onActivityResult() when the activity exits.
     * @return true if successful, otherwise is false.
     */
    fun getIntent(context: Context): Intent {
        var intent = when {
            MARK.contains("huawei") -> huaweiApi(context)
            MARK.contains("xiaomi") -> xiaomiApi(context)
            MARK.contains("oppo") -> oppoApi(context)
            MARK.contains("vivo") -> vivoApi(context)
            MARK.contains("meizu") -> meizuApi(context)
            else -> defaultApi(context)
        }
        val isAvali = context.packageManager?.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)?.size ?: 0 > 0
        return if (isAvali) {
            intent
        } else
            return defaultApi(context)
    }

    private fun defaultApi(context: Context): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        return intent
    }

    private fun huaweiApi(context: Context): Intent {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return defaultApi(context)
        }
        val intent = Intent()
        intent.component = ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity")
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
            8 -> {
                val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
                intent.putExtra("extra_pkgname", context.packageName)
            }

        }
        return intent
    }

    private fun vivoApi(context: Context): Intent {
        val intent = Intent()
        intent.putExtra("packagename", context.packageName)
        intent.component = ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity")
        if (hasActivity(context, intent)) return intent

        intent.component = ComponentName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity")
        return intent
    }

    private fun oppoApi(context: Context): Intent {
        val intent = Intent()
        intent.putExtra("packageName", context.packageName)
        intent.component = ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity")
        return intent
    }

    private fun meizuApi(context: Context): Intent {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return defaultApi(context)
        }
        val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
        intent.putExtra("packageName", context.packageName)
        intent.component = ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity")
        return intent
    }

    private fun hasActivity(context: Context, intent: Intent): Boolean {
        val packageManager = context.packageManager
        return packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size > 0
    }
}