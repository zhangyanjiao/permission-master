package com.magic.module.permission.keep



/**
 * @author zhangyanjiao
 * @data on 2018/6/27 18:10
 * @desc RequestExecutor.kt
 */
interface RequestExecutor {
    fun executor(deniedPermissionList: ArrayList<String>)
}
