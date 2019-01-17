package com.qihoo.security.permissionManager.anim

import android.animation.Animator
import android.util.Log
import com.magic.module.permission.demo.BuildConfig

/**
 * @author zhangyanjiao
 * @data on 2018/7/2 17:41
 * @desc IAnimListener.kt
 */
abstract class IAnimListener : Animator.AnimatorListener {
    var DEBUG = BuildConfig.DEBUG
    var TAG = "IAnimListener"
    override fun onAnimationRepeat(animation: Animator?) {
        if (DEBUG) {
            Log.d(TAG, "onAnimationRepeat");
        }
    }

    override fun onAnimationEnd(animation: Animator?) {
        if (DEBUG) {
            Log.d(TAG, "onAnimationEnd");
        }
    }

    override fun onAnimationCancel(animation: Animator?) {
        if (DEBUG) {
            Log.d(TAG, "onAnimationCancel");
        }
    }

    override fun onAnimationStart(animation: Animator?) {
        if (DEBUG) {
            Log.d(TAG, "onAnimationStart");
        }
    }
}