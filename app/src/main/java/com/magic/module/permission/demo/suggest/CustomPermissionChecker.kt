package com.magic.module.permission.demo.suggest

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import com.magic.module.permission.demo.suggest.permissionDialog.CleanPermissionDialog
import com.magic.module.permission.demo.suggest.permissionDialog.SimpleBaseDialog
import com.magic.module.permission.keep.PermissionPager
import com.magic.module.permission.keep.RequestExecutor

/**
 * @author zhangyanjiao
 * @data on 2018/9/3 20:28
 * @desc CustomPermissionChecker.kt
 */
object CustomPermissionChecker {
    fun checkPermission(activity: Activity, @PermissionFuncType type: String, callBack: PermissionCallback?) {
        dialogList?.forEach {
            Log.d("aaa", "it")
        }
        if (dialogList?.get(type) == null) {
            if (!PermissionUtils.checkPermissionAllow(activity, type)) {
                PermissionUtils.requestPermission(activity, type, true, object : PermissionCallback() {
                    override fun onRationale(data: List<String>, requestExecutor: RequestExecutor) {
                        val permissionSuggestDialog = getPermissionSuggestDialog(activity, type)
                        recodeDialog(type, permissionSuggestDialog)
                        permissionSuggestDialog?.show(object : SimpleBaseDialog.Callback {
                            override fun dismiss() {
                                removeDialog(type, permissionSuggestDialog)
                            }

                            override fun confirm() {
                                requestExecutor.executor(data as ArrayList<String>)
                                removeDialog(type, permissionSuggestDialog)
                            }

                            override fun cancle() {
                                removeDialog(type, permissionSuggestDialog)
                            }
                        })

                    }

                    override fun onGranted() {
                        callBack?.onGranted()
                    }

                    override fun onDenied() {
                        Toast.makeText(activity, "onDenied", Toast.LENGTH_SHORT).show()
                        callBack?.onDenied()
                    }

                    override fun onNoAskDenied(context: Context, strings: MutableList<String>) {
                        callBack?.onDenied()
                        var intent = PermissionPager.getIntent(context, strings)
                        if (!checkIntentEffect(context, intent)) {
                            intent = PermissionPager.getIntent(context)
                        }
                        try {
                            activity.startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
            } else {
                callBack?.onGranted()
            }
        }
    }

    private val dialogList: MutableMap<String, CleanPermissionDialog>? = mutableMapOf()
    private fun recodeDialog(@PermissionFuncType type: String, dialog: CleanPermissionDialog?) {
        dialog?.let {
            dialogList?.put(type, it)
        }
    }

    private fun removeDialog(type: String, dialog: CleanPermissionDialog) {
        if (dialogList?.containsKey(type) == true) {
            dialogList.remove(type)
            dialog.dismiss()
        }
    }

    fun removeAll() {
        dialogList?.forEach {
            it.value.dismiss()
        }

        dialogList?.clear()
    }

    fun getPermissionSuggestDialog(activity: Activity, @PermissionFuncType type: String): CleanPermissionDialog? {
        var permissionSuggestDialog = CleanPermissionDialog(activity)
        permissionSuggestDialog.build()
        permissionSuggestDialog.setPermissionType(type)
        return permissionSuggestDialog
    }


    fun checkUsageStateExist(context: Context): Boolean {
        val intent = PermissionPager.getIntent(context, Manifest.permission.PACKAGE_USAGE_STATS)
        return checkIntentEffect(context, intent)
    }

    /**
     * 检测Intent是否有效
     */
    fun checkIntentEffect(context: Context, intent: Intent): Boolean {
        val queryIntentActivities =
                context.packageManager?.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return queryIntentActivities != null && !queryIntentActivities.isEmpty()
    }

}
