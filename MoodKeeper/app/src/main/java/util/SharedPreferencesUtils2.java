package util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPreferencesUtils2 {

    private Context context;
    private static String defaultName = "CONFIG";

    public SharedPreferencesUtils2(Context ctx, String name) {
        defaultName = name;
        this.context = ctx;
    }

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public void putString(String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    /**
     * SP中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public String getString(String key) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }


    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     */
    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    /**
     * SP中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }

    public int getInt(String key) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }

    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     */
    public static void putLong(Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).apply();
    }

    public void putLong(String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).apply();
    }

    /**
     * SP中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public static long getLong(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sp.getLong(key, -1L);
    }

    public long getLong(String key) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        return sp.getLong(key, -1L);
    }

    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     */
    public static void putFloat(Context context, String key, float value) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        sp.edit().putFloat(key, value).apply();
    }

    public void putFloat(String key, float value) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        sp.edit().putFloat(key, value).apply();
    }

    /**
     * SP中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public static float getFloat(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sp.getFloat(key, -1F);
    }

    public float getFloat(String key) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        return sp.getFloat(key, -1F);
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * SP中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public boolean getBoolean(String key) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * SP中移除该key
     *
     * @param key 键
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

    public void remove(String key) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

    /**
     * SP中移除所有内容
     *
     * @param name sp文件名字
     */
    public static void clearAll(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public void clearAll(String name) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public void putObject(String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        String json = new Gson().toJson(value);
        sp.edit().putString(key, json).apply();
    }

    public Object getObject(String key, Class clazz) {
        SharedPreferences sp = context.getSharedPreferences(defaultName, Context.MODE_PRIVATE);
        String json = sp.getString(key, null);
        return new Gson().fromJson(json, clazz);
    }

}
