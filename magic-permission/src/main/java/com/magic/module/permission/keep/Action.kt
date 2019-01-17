package com.magic.module.permission.keep


/**
 * @author zhangyanjiao
 * @data on 2018/6/27 14:49
 * @desc Action.kt
 */
interface Action<in T> {
    fun onAction(data: T)
}