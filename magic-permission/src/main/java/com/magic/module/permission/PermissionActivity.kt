package com.magic.module.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.PermissionChecker
import com.magic.module.permission.request.PermissionCallBack

class PermissionActivity : Activity() {


    private val VALUE_INPUT_PERMISSION: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        val permissions = intent.getStringArrayListExtra("key_input_permissions")
        if (permissions != null && mRequest != null) {
            val targetVersion = applicationInfo.targetSdkVersion
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && targetVersion >= 23) {
                requestPermissions(permissions.toArray(emptyArray<String>()), VALUE_INPUT_PERMISSION)
            } else {
                permissions.forEach {
                    PermissionChecker.checkSelfPermission(this, it)
                }
                mRequest?.handleRequest()
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        mRequest?.handleRequest()
        finish()
    }

    companion object {
        private var mRequest: PermissionCallBack? = null
        fun requestPermission(context: Context, permissions: ArrayList<String>, request: PermissionCallBack) {
            mRequest = request
            val intent = Intent(context, PermissionActivity::class.java)
            intent.putExtra("key_input_permissions", permissions)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

}
