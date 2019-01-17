package com.magic.module.permission.keep


/**
 * @author zhangyanjiao
 * @data on 2018/6/27 18:15
 * @desc RationaleAction.kt
 */
interface RationaleAction<in T> {
    fun action(data: T, requestExecutor: RequestExecutor)
}
