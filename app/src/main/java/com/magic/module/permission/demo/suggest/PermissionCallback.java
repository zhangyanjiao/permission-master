package com.magic.module.permission.demo.suggest;

import android.content.Context;
import android.content.Intent;

import com.magic.module.permission.keep.PermissionPager;
import com.magic.module.permission.keep.RequestExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyanjiao
 * @data on 2018/6/29 11:13
 * @desc PermissionCallback.java
 */
public abstract class PermissionCallback {
    /**
     * 引导规则 有需要授权的权限弹窗弹出之前 可以添加一些引导提示等
     * 默认操作 执行请求权限
     */
    public void onRationale(List<String> data, RequestExecutor requestExecutor) {
        requestExecutor.executor((ArrayList) data);
    }

    /**
     * 权限允许操作
     * 默认无操作
     */
    public void onGranted() {
    }

    /**
     * 权限拒绝操作
     * 默认无操作
     */
    public void onDenied() {
    }

    /**
     * 权限永久拒绝操作（不再提示）
     * 默认实现跳转setting
     */
    public void onNoAskDenied(Context context, List<String> strings) {
        Intent intent = PermissionPager.INSTANCE.getIntent(context, strings);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }
}
