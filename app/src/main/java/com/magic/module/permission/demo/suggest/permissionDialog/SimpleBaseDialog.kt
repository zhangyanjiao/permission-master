package com.magic.module.permission.demo.suggest.permissionDialog

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.text.TextUtils
import android.view.*
import com.magic.module.permission.demo.R


/**
 * 简单mDialog  默认从底部出现
 */
abstract class SimpleBaseDialog(val mActivity: Activity) : DialogInterface.OnCancelListener,
        DialogInterface.OnDismissListener {

    var TAG = "SimpleBaseDialog"

    var callback: Callback? = null
    /**
     * 设置弹窗风格
     */
    var mDialogStyle = R.style.App_Dialog

    /**
     * 设置对齐方式
     */
    var mDialogGravity = Gravity.BOTTOM


    lateinit var contentView: View

    var mPermissionType: String? = null


    var mDialog: Dialog? = null

    fun build(): Dialog? {
        val inflater = mActivity.layoutInflater
        if (inflater != null) {
            contentView = getView(inflater)
                    ?: throw IllegalStateException("please implement getView()方法")
            if (!TextUtils.isEmpty(mPermissionType)) {
                setPermissionType(mPermissionType!!)
            }
            mDialog = Dialog(mActivity, mDialogStyle)
            mDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mDialog?.setContentView(contentView)
            mDialog?.setCanceledOnTouchOutside(true)
            // 设置宽度为屏宽, 靠近屏幕底部。
            val window = mDialog?.window
            val lp = window?.attributes
            lp?.gravity = mDialogGravity
            setLayoutParams(lp)
            window?.attributes = lp
            mDialog?.setOnCancelListener(this)
            mDialog?.setOnDismissListener(this)
        }
        return mDialog
    }

    protected open fun setLayoutParams(lp: WindowManager.LayoutParams?) {
        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
    }


    /**
     * 自定义View
     */
    abstract fun getView(inflater: LayoutInflater): View?


    /**
     * 展示弹窗
     */
    open fun show(callback: Callback?) {
        this.callback = callback
        mDialog?.show()
    }

    open fun dismiss() {
        mDialog?.dismiss()
        callback?.dismiss()
        this.callback = null
    }

    abstract fun setPermissionType(type: String)

    override fun onCancel(dialog: DialogInterface?) {
        callback?.cancle()
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        dismiss()
    }


    interface Callback {
        fun confirm()
        fun cancle()
        fun dismiss()
    }
}