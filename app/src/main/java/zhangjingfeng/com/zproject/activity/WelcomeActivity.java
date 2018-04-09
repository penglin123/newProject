package zhangjingfeng.com.zproject.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import zhangjingfeng.com.zproject.MainActivity;
import zhangjingfeng.com.zproject.MyApplication;
import zhangjingfeng.com.zproject.R;

public class WelcomeActivity extends Activity {
    private Button timerBtn;
    private int count = 2;
    private boolean tag = true;
    private ImageView welcomeImg;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_welcome);//设置页面布局
        welcomeImg = (ImageView) findViewById(R.id.welcome_img);
        //设置渐变动画
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.welcome);
//        welcomeImg.startAnimation(animation);

        timerBtn = (Button) findViewById(R.id.timer_btn);
        timerBtn.setText(count + "s");
        initPermission();
        sp=getSharedPreferences("loginInfo",MODE_PRIVATE);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            count--;
            timerBtn.setText(count + "s");
            if (count == 0) {
                startLogin();
            } else {
                handler.sendEmptyMessageDelayed(0, 1 * 1000);
            }

        }
    };

    /**
     * 判断是否转到登陆
     */
    public void startLogin() {
        Intent intent=new Intent();
        if(sp.getBoolean("isLogin",false)) {
            intent.setClass(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        }else {
            intent.setClass(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(WelcomeActivity.this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(WelcomeActivity.this, toApplyList.toArray(tmpList), 123);
        } else {
            handler.sendEmptyMessageDelayed(0, 1 * 1000);
        }

    }

    /**
     * 权限请求回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
//                    if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {
                    tag = false;
                    break;
                }
            }
            if (tag == true) {
                handler.sendEmptyMessageDelayed(0, 1 * 1000);
            } else {
                setPermissionDialog("联系人访问");
            }
        }
    }

    //提示设置权限
    private void setPermissionDialog(String str) {
        AlertDialog alertDialog = new AlertDialog.Builder(WelcomeActivity.this)
                .setMessage("请赋予 " + str + " 的权限，不开启将无法正常使用该功能!")
                .setCancelable(false)
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData(Uri.parse("package:" + MyApplication.APPContext.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//可能压在一个新建的栈中
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);//activity不压在栈中
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);//从任务列表中清除
                        startActivity(intent);
                        finish();
                    }
                })
                .create();
        alertDialog.show();
    }
}
