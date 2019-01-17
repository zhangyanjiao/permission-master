package com.magic.module.permission.demo.suggest.permissionDialog

import android.app.Activity
import android.content.DialogInterface
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.magic.module.permission.demo.R


/**
 * @author zhangyanjiao
 * @data on 2018/6/29 17:47
 * @desc CleanPermissionDialog.kt
 */
class CleanPermissionDialog : SimpleBaseDialog {
    private var mIsConfirm: Boolean = false


    private var mIconView: ImageView? = null

    constructor(activity: Activity) : super(activity) {
        mDialogGravity = Gravity.BOTTOM
    }

    override fun getView(inflater: LayoutInflater): View? {
        val view = inflater.inflate(R.layout.dialog_clean_permission_suggest, null)
        mIconView = view.findViewById(R.id.iv_icon) as ImageView
        val a: View = view.findViewById(R.id.bt_continue)
        a.setOnClickListener {
            mIsConfirm = true
            callback?.confirm()
            dismiss()
        }
        return view
    }


    override fun setPermissionType(type: String) {
//        val permissionDetailName = PermissionMapUtils.getPermissionDetailName(type)
//        if (permissionDetailName.isNotEmpty()) {
//            permissionDetailName.forEach {
//                if (it.value != null) {
//                    mIconView?.setImageResource(it.value.suggestIconRes)
//                    mMsgView?.text = it.value.suggestMsgRes
//                    return
//                }
//            }
//        }
    }

    fun show() {
        mIsConfirm = false
        super.show(null)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        if (!mIsConfirm) {
            callback?.cancle()
        }
        super.onDismiss(dialog)
    }

}