package com.magic.module.permission.utils

import android.app.AppOpsManager
import android.os.Build
import android.support.annotation.RequiresApi

/**
 * @author zhangyanjiao
 * @data on 2018/9/6 18:40
 * @desc PermissioResultChecker.ktr.kt
 */
object PermissioResultChecker {
    private val MARK = Build.MANUFACTURER.toLowerCase()

    /**
     * 检测权限结果
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun checkResult(op: String, result: Int): Boolean {
        when (op) {
            android.Manifest.permission.SYSTEM_ALERT_WINDOW -> {
                return checkAlertResult(result)
            }
            else -> {
                return AppOpsManager.MODE_ALLOWED == result
            }
        }

    }

    /**
     * 临时针对小米手机处理
     * MARK.contains("huawei")
    MARK.contains("oppo")
    MARK.contains("vivo")
    MARK.contains("meizu")
     */
    private fun checkAlertResult(result: Int): Boolean {
        return when {
            MARK.contains("xiaomi") -> {
                AppOpsManager.MODE_ALLOWED == result
            }
            else -> {
                AppOpsManager.MODE_ALLOWED == result || AppOpsManager.MODE_DEFAULT == result
            }
        }
    }

}
