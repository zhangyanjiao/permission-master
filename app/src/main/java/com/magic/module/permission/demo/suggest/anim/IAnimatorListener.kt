package com.qihoo.security.permissionManager.anim

import android.util.Log
import com.magic.module.permission.demo.BuildConfig
import com.nineoldandroids.animation.Animator

/**
 * @author zhangyanjiao
 * @data on 2018/7/3 18:32
 * @desc IAnimatorListener.kt
 */
open class IAnimatorListener : Animator.AnimatorListener {
    var DEBUG = BuildConfig.DEBUG
    var TAG = "IAnimatorListener"
    override fun onAnimationRepeat(animation: Animator?) {
        if (DEBUG) {
            Log.d(TAG, "")
        }
    }

    override fun onAnimationEnd(animation: Animator?) {
        if (DEBUG) {
            Log.d(TAG, "")
        }
    }

    override fun onAnimationCancel(animation: Animator?) {
        if (DEBUG) {
            Log.d(TAG, "")
        }
    }

    override fun onAnimationStart(animation: Animator?) {
        if (DEBUG) {
            Log.d(TAG, "")
        }
    }

}