package com.linsaya.heima_zhbj_01.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/1/15.
 */

public class PrefUtils {

    public static boolean getBoolean(Context ctx, String name, boolean defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean(name, defValue);
    }

    public static void putBoolean(Context ctx, String name, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putBoolean(name, value).commit();
    }

    public static String getString(Context ctx, String name, String defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getString(name, defValue);
    }

    public static void putString(Context ctx, String name, String value) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putString(name, value).commit();
    }

    public static int getInt(Context ctx, String name, int defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getInt(name, defValue);
    }

    public static void putInt(Context ctx, String name, int value) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putInt(name, value).commit();
    }
}
