package zhangjingfeng.com.zproject;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.Utils;

import java.util.ArrayList;
import java.util.List;

import zhangjingfeng.com.zproject.api.ApiHelper;
import zhangjingfeng.com.zproject.api.ApiService;


public class MyApplication extends Application {
    public static int H, W;
    public static Context APPContext;
    public static ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        APPContext = getApplicationContext();
        getScreen(this);
        //接口服务初始化
        apiService = ApiHelper.getInstance(APPContext).getService();
        //工具库初始化
        Utils.init(this);

    }

    //获取屏幕尺寸
    public void getScreen(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        H = dm.heightPixels;
        W = dm.widthPixels;
    }
}
