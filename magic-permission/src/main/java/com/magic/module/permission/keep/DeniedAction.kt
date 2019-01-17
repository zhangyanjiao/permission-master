package com.magic.module.permission.keep

/**
 * @author zhangyanjiao
 * @data on 2018/6/27 14:49
 * @desc Action.kt
 */
abstract class DeniedAction<in T> : Action<T> {

    /**
     * @param data :返回数据
     * @param isShowCustomView 用户选择不再提示 是否进行跳转或者提示  true 可以  false 不可以
     */
    abstract fun onAction(data: T, isShowCustomView: Boolean)

    override fun onAction(data: T) {
        onAction(data, true)
    }
}