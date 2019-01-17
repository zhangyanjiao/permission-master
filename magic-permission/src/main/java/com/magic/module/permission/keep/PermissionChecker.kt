package com.magic.module.permission.keep

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import com.magic.module.permission.utils.PermissioResultChecker
import com.magic.module.permission.utils.PermissionTransUtils
import java.util.*
import kotlin.collections.ArrayList


/**
 * @author zhangyanjiao
 * @data on 2018/6/27 16:40
 * @desc PermissionChecker.kt
 */
object PermissionChecker {

    /**
     * 重新获取当前用户没有授予的权限（为了解决23以下版本权限弹窗问题）
     */
    fun getDeniedPermissions(mPermissions: List<String>?, mContext: Context): ArrayList<String> {
        val deniedList = ArrayList<String>()

        mPermissions?.forEach {
            val allow = checkSelfPermission(mContext, it)
            if (!allow) {
                deniedList.add(it)
            }
        }
        return deniedList
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun checkPermission(context: Context, op: String): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= 19) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val result = manager.checkOp(op, Binder.getCallingUid(), context.packageName)
                return PermissioResultChecker.checkResult(PermissionTransUtils.getInstance().getPermissionNameByOp(op), result)
            } catch (e: Exception) {
            }

        }
        return true
    }

    fun checkSelfPermission(mContext: Context, it: String): Boolean {
        val targetVersion = mContext.applicationInfo.targetSdkVersion
        when (it) {
            Manifest.permission.WRITE_SETTINGS -> {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Settings.System.canWrite(mContext)
                } else {
                    true
                }
            }
            Manifest.permission.ACCESS_NOTIFICATION_POLICY -> {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val notificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.isNotificationPolicyAccessGranted
                } else {
                    true
                }
            }
            Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    val pkgName = mContext.packageName
                    val flat = Settings.Secure.getString(mContext.contentResolver, "enabled_notification_listeners")
                    if (!flat.isNullOrEmpty()) {
                        val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        names.forEach {
                            val cn = ComponentName.unflattenFromString(it)
                            if (cn != null && pkgName == cn.packageName) {
                                return true
                            }
                        }
                    }
                    return false
                }
                return true
            }
            else -> {
                if (Build.VERSION.SDK_INT >= 23 && targetVersion >= 23 && it == Manifest.permission.SYSTEM_ALERT_WINDOW) {
                    if (!Settings.canDrawOverlays(mContext)) {
                        return false
                    }
                }
                val op = PermissionTransUtils.getInstance().getOPByPermissionName(it)
                if (!op.isNullOrEmpty()) {
                    if (!checkPermission(mContext, op!!)) {
                        return false
                    }
                } else if (Build.VERSION.SDK_INT >= 23 && targetVersion >= 23) {
                    if (ContextCompat.checkSelfPermission(mContext, it) != PackageManager.PERMISSION_GRANTED) {
                        return false
                    }
                } else {
                    try {
                        val permission = PermissionChecker.checkSelfPermission(mContext, it)
                        if (permission == PermissionChecker.PERMISSION_DENIED_APP_OP || permission == PermissionChecker.PERMISSION_DENIED) {
                            return false
                        }
                    } catch (e: Throwable) {
                        return false
                    }

                }
                return true

            }
        }

    }

    /**
     * 获取当前清单文件中注册的权限
     */
    fun getManifestPermissions(context: Context): List<String> {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
            val permissions = packageInfo.requestedPermissions
            if (permissions == null || permissions.isEmpty()) {
                throw Throwable("You did not register any permissions in the manifest.xml.")
            }
            return Collections.unmodifiableList(Arrays.asList(*permissions))
        } catch (e: PackageManager.NameNotFoundException) {
            throw AssertionError("Package name cannot be found.")
        }

    }

    fun getPermissionSelectNoAsk(context: Context, mPermissions: List<String>?): ArrayList<String> {
        val noAskList = ArrayList<String>()
        mPermissions?.forEach {
            if (!isShowRationalePermission(context, it)) {
                noAskList.add(it)
            }
        }
        return noAskList
    }

    fun isShowRationalePermission(mContext: Context, permission: String): Boolean {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && mContext is Activity) {
            return mContext.shouldShowRequestPermissionRationale(permission)
        }
        val packageManager = mContext.packageManager
        val pkManagerClass = packageManager.javaClass
        return try {
            val method = pkManagerClass.getMethod("shouldShowRequestPermissionRationale", String::class.java)
            if (!method.isAccessible) method.isAccessible = true
            method.invoke(packageManager, permission) as Boolean
        } catch (ignored: Exception) {
            false
        }

    }

}
