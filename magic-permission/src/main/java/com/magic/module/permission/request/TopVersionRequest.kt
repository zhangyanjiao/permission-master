package com.magic.module.permission.request


import android.content.Context
import com.magic.module.permission.PermissionActivity
import com.magic.module.permission.keep.*
import com.magic.module.permission.utils.PermissionKit

/**
 * @author zhangyanjiao
 * @data on 2018/6/27 15:27
 * @desc SDK_INT>23 权限实现
 */

class TopVersionRequest : IRequest, PermissionCallBack, RequestExecutor {

    var mPermissions: ArrayList<String>? = null
    var mGranted: Action<List<String>>? = null
    var mDenied: Action<List<String>>? = null
    var mRationale: RationaleAction<List<String>>? = null
    var mContext: Context? = null
    override fun permission(vararg permissions: String): IRequest {
        if (mPermissions == null) {
            mPermissions = ArrayList()
        } else {
            mPermissions?.clear()
        }
        permissions.forEach {
            mPermissions?.add(it)
        }
        return this
    }

    override fun permission(permissions: List<String>): IRequest {
        if (mPermissions == null) {
            mPermissions = ArrayList()
        } else {
            mPermissions?.clear()
        }
        permissions.forEach {
            mPermissions?.add(it)
        }
        return this
    }

    override fun onRationale(granted: RationaleAction<List<String>>): IRequest {
        mRationale = granted
        return this
    }

    override fun onGranted(granted: Action<List<String>>): IRequest {
        mGranted = granted
        return this
    }

    override fun onDenied(denied: Action<List<String>>): IRequest {
        mDenied = denied
        return this
    }


    private var lastPermissionSelectNoAsk: ArrayList<String> = ArrayList()

    override fun request() {
        mContext = PermissionKit.context
        if (mContext == null) {
            throw IllegalStateException("mContext can not empty,please use PermissionRequest.with(context) before request")
        }
        mContext?.let {
            val context = it
            val allRegistPermission = PermissionChecker.getManifestPermissions(context)
            mPermissions?.let {
                val permissions = it.clone() as ArrayList<String>
                permissions.forEach {
                    if (!allRegistPermission.contains(it)) {
                        throw IllegalArgumentException("$it permission is not registe in Manifest.xml")
                    }
                }
                var needRequestPermissionList = PermissionChecker.getDeniedPermissions(mPermissions, context)
                if (needRequestPermissionList.isEmpty()) {
                    handleRequest()
                } else {
                    lastPermissionSelectNoAsk = PermissionChecker.getPermissionSelectNoAsk(context, needRequestPermissionList)
                    if (mRationale != null) {
                        mRationale?.action(needRequestPermissionList, this)
                    } else {
                        executor(needRequestPermissionList)
                    }
                }
            }
        }
    }


    /**
     * 执行权限请求
     */
    override fun executor(deniedPermissionList: ArrayList<String>) {
        if (mContext == null) {
            throw IllegalStateException("mContext is killed")
        }
        mContext?.let {
            PermissionActivity.requestPermission(it, deniedPermissionList, this)
        }
    }


    /**
     * 处理权限请求结果
     */
    override fun handleRequest() {
        if (mContext == null) {
            throw IllegalStateException("mContext is killed")
        }
        mContext?.let {
            val deniedPermissionList = PermissionChecker.getDeniedPermissions(mPermissions, it)
            if (deniedPermissionList.isEmpty()) {
                mGranted?.onAction(mPermissions as List<String>)
            } else {
                if (mDenied != null && mDenied is DeniedAction<List<String>>) {
                    val permissionSelectNoAsk = PermissionChecker.getPermissionSelectNoAsk(it, deniedPermissionList)
                    (mDenied as DeniedAction<List<String>>).onAction(deniedPermissionList, !lastPermissionSelectNoAsk.isEmpty() && !permissionSelectNoAsk.isEmpty())
                } else {
                    mDenied?.onAction(deniedPermissionList)
                }
            }
        }
    }

}