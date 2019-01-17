package com.magic.module.permission.demo

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.magic.module.permission.demo.suggest.CustomPermissionChecker
import com.magic.module.permission.demo.suggest.PermissionCallback
import com.magic.module.permission.demo.suggest.PermissionFuncType
import com.magic.module.permission.demo.suggest.PermissionUtils


class MainActivity : Activity(), View.OnClickListener {

    private lateinit var mContext: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this
        findViewById<Button>(R.id.bt_access_one_permission).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_access_one_permission -> {
                val checkPermissionAllow = PermissionUtils.checkPermissionAllow(mContext, PermissionFuncType.CLEANFUNC)
                Toast.makeText(this, "获取单个权限$checkPermissionAllow", Toast.LENGTH_SHORT).show()
                CustomPermissionChecker.checkPermission(this, PermissionFuncType.CLEANFUNC, object : PermissionCallback() {})
            }
        }
    }
}
