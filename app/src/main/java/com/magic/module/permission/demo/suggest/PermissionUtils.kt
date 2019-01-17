package com.magic.module.permission.demo.suggest

import android.content.Context
import com.magic.module.permission.keep.*

/**
 * @author zhangyanjiao
 * @data on 2018/6/29 10:23
 * @desc PermissionUtils.kt
 */
object PermissionUtils {
    /**
     * @param context 上下文 任意上下文
     * @param type 当前功能的权限 需要再PermissionMapUtils中进行添加
     * @param isNoAskDeniedShowSuggest 是否当用户选择不再询问 拒绝之后 再请求权限的特殊操作操作（true 需要执行特殊操作  false 不需要）
     * @param permissionCallback 权限结果回调
     */
    fun requestPermission(context: Context, @PermissionFuncType type: String, isNoAskDeniedShowSuggest: Boolean, permissionCallback: PermissionCallback?) {
        PermissionRequest.with(context)
                ?.permission(PermissionMapUtils.getPermissionByFunc(type))
                ?.onRationale(object : RationaleAction<List<String>> {
                    override fun action(data: List<String>, requestExecutor: RequestExecutor) {
                        permissionCallback?.onRationale(data, requestExecutor)
                    }

                })
                ?.onGranted(object : com.magic.module.permission.keep.Action<List<String>> {
                    override fun onAction(strings: List<String>) {
                        permissionCallback?.onGranted()
                    }
                })
                ?.onDenied(object : DeniedAction<List<String>>() {
                    override fun onAction(strings: List<String>, isNoAskDenied: Boolean) {
                        if (isNoAskDeniedShowSuggest && isNoAskDenied) {
                            permissionCallback?.onNoAskDenied(context, strings)
                        } else {
                            permissionCallback?.onDenied()
                        }
                    }

                })?.request()
    }

    fun requestPermission(context: Context, vararg  permissions: String, isNoAskDeniedShowSuggest: Boolean, permissionCallback: PermissionCallback?) {
        PermissionRequest.with(context)
                ?.permission(permissions.asList())
                ?.onRationale(object : RationaleAction<List<String>> {
                    override fun action(data: List<String>, requestExecutor: RequestExecutor) {
                        permissionCallback?.onRationale(data, requestExecutor)
                    }

                })
                ?.onGranted(object : com.magic.module.permission.keep.Action<List<String>> {
                    override fun onAction(strings: List<String>) {
                        permissionCallback?.onGranted()
                    }
                })
                ?.onDenied(object : DeniedAction<List<String>>() {
                    override fun onAction(strings: List<String>, isNoAskDenied: Boolean) {
                        if (isNoAskDeniedShowSuggest && isNoAskDenied) {
                            permissionCallback?.onNoAskDenied(context, strings)
                        } else {
                            permissionCallback?.onDenied()
                        }
                    }

                })?.request()
    }


    /**
     * 检测当前功能所有权限是否被赋予
     */
    fun checkPermissionAllow(context: Context, type: String): Boolean {
        val deniedPermissions = PermissionChecker.getDeniedPermissions(PermissionMapUtils.getPermissionByFunc(type), context)
        return deniedPermissions.isEmpty()
    }

    fun checkSinglePermissionAllow(context: Context, permission: String): Boolean {
        return PermissionChecker.checkSelfPermission(context, permission)
    }
}