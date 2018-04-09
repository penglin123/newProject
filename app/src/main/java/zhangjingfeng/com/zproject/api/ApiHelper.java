package zhangjingfeng.com.zproject.api;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit相关
 * Created by Administrator on 2017/11/9.
 */

public class ApiHelper {
    public static ApiHelper instance;
    private ApiService service;
    private Context mContext;


    public ApiHelper(Context context) {
        mContext=context;
        init();
    }

    private void init() {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service=retrofit.create(ApiService.class);//获取接口示例

    }

    public static ApiHelper getInstance(Context context) {
        if(instance==null){
            instance=new ApiHelper(context);
        }
        return instance;
    }

    public ApiService getService() {
        return service;
    }
}
