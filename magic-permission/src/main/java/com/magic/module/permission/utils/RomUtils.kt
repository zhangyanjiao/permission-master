package com.magic.module.permission.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object RomUtils {
    /**
     * 获取小米 rom 版本号，获取失败返回 -1
     *
     * @return miui rom version code, if fail , return -1
     */
    fun getMiuiVersion(): Int {
        val version = getSystemProperty("ro.miui.ui.version.name")
        if (version != null) {
            try {
                return Integer.parseInt(version.substring(1))
            } catch (e: Exception) {
//                e.printStackTrace()
            }

        }
        return -1
    }

    fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                }
            }
        }
        return line
    }
}
