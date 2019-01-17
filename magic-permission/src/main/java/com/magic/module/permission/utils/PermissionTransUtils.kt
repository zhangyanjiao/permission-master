package com.magic.module.permission.utils

import android.Manifest
import android.app.AppOpsManager

/**
 * @author zhangyanjiao
 * @data on 2018/9/7 15:00
 * @desc 只做了部分权限转换  如果有时间继续填充
 */

class PermissionTransUtils {
    private var permissionTypeMap: HashMap<String, String> = HashMap()

    init {
        permissionTypeMap[Manifest.permission.SYSTEM_ALERT_WINDOW] = AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW
        permissionTypeMap[Manifest.permission.PACKAGE_USAGE_STATS] = AppOpsManager.OPSTR_GET_USAGE_STATS
    }


    companion object {
        fun getInstance() = PermissionTransUtils()
    }

    /**
     * 获取权限再AppOpsManager中对应值
     */
    fun getOPByPermissionName(permission: String): String? {
        return permissionTypeMap[permission] ?: ""

    }


    fun getPermissionNameByOp(op: String): String {
        permissionTypeMap.forEach {
            if (it.value == op) {
                return it.key
            }
        }
        return ""
    }
}
