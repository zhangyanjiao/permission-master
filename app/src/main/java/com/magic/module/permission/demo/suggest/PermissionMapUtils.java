package com.magic.module.permission.demo.suggest;

import android.Manifest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyanjiao
 * @data on 2018/6/28 17:08
 * @desc PermissionMapUtils.java
 */
public class PermissionMapUtils {
    private static ArrayList<String> cleanPermissionList = new ArrayList<>();
    private static ArrayList<String> blockMainPermissionList = new ArrayList<>();

    static {
//        cleanPermissionList.add(Manifest.permission.WRITE_SETTINGS);
//        cleanPermissionList.add(Manifest.permission.PACKAGE_USAGE_STATS);
//        cleanPermissionList.add(Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE);
        cleanPermissionList.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
//        cleanPermissionList.add(Manifest.permission.ACCESS_NOTIFICATION_POLICY);

//        cleanPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
////        cleanPermissionList.add(Manifest.permission.CALL_PHONE);
////        cleanPermissionList.add(Manifest.permission.READ_CALL_LOG);
////        cleanPermissionList.add(Manifest.permission.WRITE_CALL_LOG);
////        cleanPermissionList.add(Manifest.permission.CALL_PHONE);
//       cleanPermissionList.add(Manifest.permission.READ_SMS);
//        cleanPermissionList.add(Manifest.permission.RECEIVE_SMS);
//
//        cleanPermissionList.add(Manifest.permission.CALL_PHONE);
//        cleanPermissionList.add(Manifest.permission.READ_CONTACTS);
//        cleanPermissionList.add(Manifest.permission.WRITE_CONTACTS);
//        cleanPermissionList.add(Manifest.permission.READ_CALL_LOG);
//        cleanPermissionList.add(Manifest.permission.WRITE_CALL_LOG);
//
//        blockMainPermissionList.add(Manifest.permission.CALL_PHONE);
//        blockMainPermissionList.add(Manifest.permission.READ_CALL_LOG);
//        blockMainPermissionList.add(Manifest.permission.READ_SMS);
//        blockMainPermissionList.add(Manifest.permission.RECEIVE_SMS);
    }

    public static List<String> getPermissionByFunc(@PermissionFuncType String funcType) {
        switch (funcType) {
            case PermissionFuncType.CLEANFUNC:
                return cleanPermissionList;
            case PermissionFuncType.BLOCK_MAIN:
                return blockMainPermissionList;
            default:
                return new ArrayList<>();
        }
    }
}
