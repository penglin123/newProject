package zhangjingfeng.com.zproject.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import zhangjingfeng.com.zproject.MainActivity;
import zhangjingfeng.com.zproject.R;
import zhangjingfeng.com.zproject.utils.ConnectionUtils;
import zhangjingfeng.com.zproject.utils.HttpUtils;
import zhangjingfeng.com.zproject.utils.LogUtils;
import zhangjingfeng.com.zproject.view.ClearEditText;

/**
 * Created by Administrator on 2018/2/5.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ClearEditText userNameCet, passwordCet;
    private TextView registerTv, changePasswordTv;
    private Button faceLoginBtn, loginBtn;
    private SharedPreferences sp;
    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameCet = (ClearEditText) findViewById(R.id.username);
        passwordCet = (ClearEditText) findViewById(R.id.password);
        registerTv = (TextView) findViewById(R.id.register);
        changePasswordTv = (TextView) findViewById(R.id.change_password);
        faceLoginBtn = (Button) findViewById(R.id.face_login);
        faceLoginBtn.setOnClickListener(this);
        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(this);
        sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
    }

    /**
     * 登陆验证
     *
     * @param json 相关参数
     */
    private void login(final JSONObject json) {
       addSubscribe(Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                subscriber.onNext(HttpUtils.httpUrlConnection(LoginActivity.this, ConnectionUtils.IPPORT + "/app/appLogin.do", json));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JSONObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JSONObject json) {
                        LogUtils.z("Login Response: " + json.toString());
                        if (getJsonInt(json, "errnum") == 0) { //登陆成功
                            sp.edit().putInt("userId", getJsonInt(json, "userId"))
                                    .putString("userName", getJsonString(json, "userName"))
                                    .putString("sessionId", getJsonString(json, "sessionId"))
                                    .putString("uuid", getJsonString(json, "uuid"))
                                    .putBoolean("isLogin",true).apply();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            LoginActivity.this.finish();
                        } else if (getJsonInt(json, "errnum") == -6) { //人脸还未注册
                            sp.edit().putString("uuid", getJsonString(json, "uuid")).apply();
                            showFaceLogin();
                        } else { //其他错误
                            passwordCet.setError(getJsonString(json, "errmsg"));

                        }
                    }
                }));
    }
    //提示注册人脸
    private void showFaceLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("该账户还未进行人脸注册，是否要进行人脸注册?");
        builder.setTitle("提示");
        builder.setPositiveButton("进行人脸识别", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent=new Intent(LoginActivity.this,FaceLoginActivity.class);
                intent.putExtra("type",FaceLoginActivity.TYPE_REGISTER);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    protected int getJsonInt(JSONObject obj, String key) {
        int res = 0;
        try {
            res = obj.getInt(key);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return res;
    }

    protected String getJsonString(JSONObject obj, String key) {
        String res = "";
        try {
            res = obj.getString(key);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.face_login:
                Intent intent=new Intent(LoginActivity.this,FaceLoginActivity.class);
                intent.putExtra("type",FaceLoginActivity.TYPE_Login);
                startActivity(intent);
                break;
            case R.id.login:
                checkLogin();
                break;
            default:
                break;
        }
    }

    /**
     * 检查登录信息合法性
     */
    private void checkLogin() {
        if(TextUtils.isEmpty(userNameCet.getText())){
            userNameCet.setError("不能为空");
            return;
        }else if (TextUtils.isEmpty(passwordCet.getText())){
            passwordCet.setError("不能为空");
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("account", userNameCet.getText().toString());
            json.put("pasword", passwordCet.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        login(json);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    private void unSubscribe(){
        if(mCompositeSubscription!=null){
            mCompositeSubscription.unsubscribe();
        }
    }
    private void addSubscribe(Subscription subscription){
        if(mCompositeSubscription==null){
            mCompositeSubscription=new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @Override
    protected void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }
}
