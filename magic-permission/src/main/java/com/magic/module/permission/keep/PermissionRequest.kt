package com.magic.module.permission.keep

import android.content.Context
import android.os.Build
import com.magic.module.permission.request.LowVersionRequest
import com.magic.module.permission.request.TopVersionRequest
import com.magic.module.permission.utils.PermissionKit

/**
 * @author zhangyanjiao
 * @data on 2018/6/27 15:00
 * @desc PermissionRequest.kt
 */
object PermissionRequest {

    private var mRequest: IRequest? = null

    init {
        mRequest = if (Build.VERSION.SDK_INT >= 23) {
            TopVersionRequest()
        } else {
            LowVersionRequest()
        }
    }

    fun with(context: Context): IRequest? {
        PermissionKit.context = context
        return mRequest
    }

    fun with(context: Context, isForceCheck: Boolean): IRequest? {
        PermissionKit.context = context
        if (Build.VERSION.SDK_INT < 23 && isForceCheck) {
            mRequest = TopVersionRequest()
        }
        return mRequest
    }
}
