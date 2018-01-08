package com.sampling;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhouchaoyuan on 2017/1/13.
 */

public class Utils {

    public static int dp2px(int dp, Context context) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static <T> List<T> asList(T... arr) {
        return arr == null ? null : new ArrayList(Arrays.asList(arr));
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean inArray(T t, List<T> list) {
        return t != null && !isEmpty(list) && list.contains(t);
    }

    public static <T> void addAll(List<T> list, T... ts) {
        List newList = Arrays.asList(ts);
        list.addAll(newList);
    }

    public static <T> int size(List<T> list) {
        int size = 0;
        if (!isEmpty(list)) {
            size = list.size();
        }

        return size;
    }

/*    public static DaoSession getDaoSession() {
        SQLiteDatabase msqLiteDatabase = AssetsDatabaseManager.getManager().getDatabase("Foodsafe.db");
        DaoMaster mdaoMaster = new DaoMaster(msqLiteDatabase);
        DaoSession mdaoSession = mdaoMaster.newSession();
        return mdaoSession;
    }*/

    //异或运算和校验
    public static byte getXor(byte[] datas) {
        byte temp = datas[0];
        for (int i = 1; i < datas.length; i++) {
            temp ^= datas[i];
        }
        return temp;
    }

    // byte转换为char
    public static char byteToChar(byte[] b) {
        char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
        return c;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String getDBpath() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator + "zodolabs" + File.separator + "db" + File.separator + "Foodsafe.db";
    }

    public static String getVideopath() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator + "zodolabs" + File.separator + "video";
    }

    public static String getFilepath() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator + "zodolabs";
    }

    public static String getApkpath() {
        String path = Environment.getExternalStorageDirectory().getPath()+File.separator+"zodolabs";
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return Environment.getExternalStorageDirectory().getPath() + File.separator + "zodolabs" + File.separator + "caiyang.apk";
    }
}
