package zhangjingfeng.com.zproject.utils;

import android.util.Log;

/**
 * 日志类
 * Created by Administrator on 2018/1/13.
 */

public class LogUtils {

    public static void z(String s){
        if(ConstantUtils.IS_LOG) {
            Log.i("zzz", s);
        }
    }
}
