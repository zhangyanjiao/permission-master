package com.magic.module.permission.keep


/**
 * @author zhangyanjiao
 * @data on 2018/6/27 14:44
 * @desc PermissionRequestkt
 */
interface IRequest {
    /**
     * 添加需要请求的权限
     * @param permissions 单个或者多个权限
     */
    fun permission(vararg permissions: String): IRequest

    /**
     * 添加需要请求的权限
     * @param permissions 单个或者多个权限
     */
    fun permission(permissions: List<String>): IRequest

    /**
     * 设置提示规则回调
     */
    fun onRationale(granted: RationaleAction<List<String>>): IRequest

    /**
     * 设置权限允许回调
     * @param granted 被允许的权限列表
     */
    fun onGranted(granted: Action<List<String>>): IRequest

    /**
     * 设置权限拒绝回调
     * @param denied 被拒绝的权限列表
     */
    fun onDenied(denied: Action<List<String>>): IRequest

    /**
     * 开始请求权限
     */
    fun request()

}