package zhangjingfeng.com.zproject.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import zhangjingfeng.com.zproject.MyApplication;
import zhangjingfeng.com.zproject.R;
import zhangjingfeng.com.zproject.activity.ActivityCollector;

/**
 * Created by Administrator on 2017/10/26.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private CompositeSubscription mCompositeSubscription;//管理Rxjava
    private Toolbar toolbar;
    private TextView title;
    public Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        Log.i(TAG, getClass().getSimpleName());//打印当前Activity
        ActivityCollector.addActivity(this);//自定义的Activity栈，入栈
        mContext=this;
        initToolbar();
        initView();
        initData();
    }

    private void initToolbar() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        title=(TextView)findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * 获取动作栏
     * @return toolbar
     */
    public Toolbar getToolbar(){
        return toolbar;
    }

    /**
     * 设置标题
     * @param s
     */
    public void setToolbarTitle(String s){
        title.setText(s);
    }

    /**
     * 需子类继承实现方法
     * @return
     */
    public abstract int getLayoutId();
    public abstract void initView();
    public abstract void initData();

    /**
     * 防止Rxjava内存溢出
     */
    protected void unSubscribe(){
        if(mCompositeSubscription!=null){
            mCompositeSubscription.unsubscribe();
        }
    }
    protected void addSubscribe(Subscription subscription){
        if(mCompositeSubscription==null){
          mCompositeSubscription=new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    /**
     * 通用Toast消息
     * @param text
     */
    public void showToast(String text){
        if(!TextUtils.isEmpty(text)){
            Toast.makeText(MyApplication.APPContext,text,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        unSubscribe();
        ActivityCollector.removeActivity(this);//出栈
        super.onDestroy();
    }
}
