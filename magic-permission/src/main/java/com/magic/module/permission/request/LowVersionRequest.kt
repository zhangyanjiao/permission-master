package com.magic.module.permission.request

import android.util.Log
import com.magic.module.permission.keep.Action
import com.magic.module.permission.keep.IRequest
import com.magic.module.permission.keep.RationaleAction

/**
 * @author zhangyanjiao
 * @data on 2018/6/27 15:36
 * @desc 低版本实现
 */

class LowVersionRequest : IRequest {
    var mPermissions: ArrayList<String>? = null
    var mGranted: Action<List<String>>? = null
    var mDenied: Action<List<String>>? = null
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
        Log.d("LowVersionRequest", "onRationale")
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

    override fun request() {
        mGranted?.onAction(mPermissions as List<String>)
    }

}