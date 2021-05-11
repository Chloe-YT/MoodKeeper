package util;

import android.content.Context;
import android.content.SharedPreferences;

import com.e.moodkeeper.constant.ModelConstant;

public class SharedPreferencesUtils {

    /**
     * 存布尔值
     * @param context  上下文
     * @param key 存的key
     * @param value  值
     *
     * */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(ModelConstant.CONFIG, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }
    /**
     * 取布尔值
     * @param context  上下文
     * @param key key
     * @param defalutValue  默认值
     *
     * */
    public static boolean getBoolean(Context context, String key, boolean defalutValue) {
        SharedPreferences sp = context.getSharedPreferences(ModelConstant.CONFIG, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defalutValue);
    }
    /**
     * 存字符串
     * @param context  上下文
     * @param key key
     * @param value  值
     *
     * */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(ModelConstant.CONFIG, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }
    /**
     * 取布尔值
     * @param context  上下文
     * @param key key
     * @param defalutValue  默认值
     *
     * */
    public static String getString(Context context, String key, String defalutValue) {
        SharedPreferences sp = context.getSharedPreferences(ModelConstant.CONFIG, Context.MODE_PRIVATE);
        return sp.getString(key, defalutValue);
    }
}
