package com.magic.module.permission.demo.suggest;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author zhangyanjiao
 * @data on 2018/6/28 17:11
 * @desc 功能权限
 */

@StringDef({
        PermissionFuncType.CLEANFUNC,
        PermissionFuncType.BLOCK_MAIN
})
@Retention(RetentionPolicy.SOURCE)
public @interface PermissionFuncType {
    String CLEANFUNC = "cleanfunc";
    String BLOCK_MAIN = "block_main";
}
