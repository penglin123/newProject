package zhangjingfeng.com.zproject.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;

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
import zhangjingfeng.com.zproject.view.CameraSurfaceView;

/**
 * Created by Administrator on 2018/2/2.
 */

public class FaceLoginActivity extends AppCompatActivity implements Camera.PictureCallback {
    private CameraSurfaceView mCameraSurPreview = null;
    private FrameLayout camera_preview;
    private Bitmap mBitmap;
    private ImageView imgPreview;
    private AVLoadingIndicatorView avi;
    private LinearLayout previewll;
    private boolean isLoading=false;
    public final static int TYPE_REGISTER = 0;
    public final static int TYPE_Login = 1;
    public int type;
    private SharedPreferences sp;
    private CompositeSubscription mCompositeSubscription;//管理Rxjava

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facelogin);
        camera_preview = (FrameLayout) findViewById(R.id.camera_preview);
        mCameraSurPreview = new CameraSurfaceView(this);
        camera_preview.addView(mCameraSurPreview, 0);
        camera_preview.setOnClickListener(myClick);
        previewll=(LinearLayout)findViewById(R.id.preview_ll);
        imgPreview = (ImageView) findViewById(R.id.image_preview);
        avi=(AVLoadingIndicatorView)findViewById(R.id.avi);
        avi.hide();
        initData();

    }

    private void initData() {
        type = getIntent().getIntExtra("type", 0);
        sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
    }

    private android.view.View.OnClickListener myClick = new android.view.View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // get an image from the camera
            if(!isLoading) {
                mCameraSurPreview.takePicture(FaceLoginActivity.this);
            }
        }
    };

    /**
     * 拍照接口回调
     *
     * @param data   照片原数据
     * @param camera
     */
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        if (null != data) {
            mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// data是字节数据，将其解析成位图
            // myCamera.stopPreview();
            // isPreview = false;

        } else {
            Toast.makeText(this, "请检查本应用摄像头权限设置", Toast.LENGTH_SHORT).show();
            return;
        }

        // 设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation",
        // 90)失效。图片竟然不能旋转了，故这里要旋转下
        Matrix matrix = new Matrix();
        matrix.postRotate((float) 270.0);

        Bitmap rotaBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
        previewll.setVisibility(View.VISIBLE);
        imgPreview.setImageBitmap(rotaBitmap);
        // 保存图片到sdcard
        if (null != rotaBitmap) {
            saveJpeg(rotaBitmap);
        } else {
            LogUtils.z("旋转图片出错");
        }

        camera.startPreview();
    }

    /**
     * 拍照数据封装
     *
     * @param bm 处理后的照片数据
     */
    /* 给定一个Bitmap，进行保存 */
    public void saveJpeg(Bitmap bm) {

        Bitmap newBM = bm.createScaledBitmap(bm, 480, 640, false);

        try {
            String ouput = encodeBase64File(Bitmap2Bytes(newBM));

            Calendar c = Calendar.getInstance();
            String year = c.get(Calendar.YEAR) + "";
            int month = c.get(Calendar.MONTH) + 1;
            String date = c.get(Calendar.DAY_OF_MONTH) + "";

            //String uuid = pref.uuid().get();
            //{"username":"473d1f924ffd4cfebdc3882a5927890f",
            // "logid":"473d1f924ffd4cfebdc3882a5927890f_2018-2-2",
            // "images":["......"],
            // "clientip":"fe80::f489:65ff:fe08:892%dummy0"}

            String uuid = "473d1f924ffd4cfebdc3882a5927890f";
            String logid = uuid + "_" + year + "-" + month + "-" + date;
            String s = "{\"username\":\"" + uuid + "\",\"logid\":\"" + logid
                    + "\",\"images\":[\"" + ouput + "\"],\"clientip\":\""
                    + getLocalIpAddress() + "\"}";

            network(s);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    void network(String s) {

        JSONObject obj = null;
        try {
            obj = new JSONObject(s);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String url = "";
        if (type == FaceLoginActivity.TYPE_REGISTER) {
            url = ConnectionUtils.IPPORT + "/app/appRegister.do";
        } else {
            url = ConnectionUtils.IPPORT + "/app/verify.do";
        }

        faceLogin(url, obj);

    }

    /**
     * 人脸验证
     *
     * @param url  请求接口地址
     * @param json 参数
     */
    private void faceLogin(final String url, final JSONObject json) {
        isLoading=true;
        avi.show();
        addSubscribe(Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                subscriber.onNext(HttpUtils.httpUrlConnection(FaceLoginActivity.this, url, json));
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
                        avi.hide();
                        isLoading=false;
                    }

                    @Override
                    public void onNext(JSONObject obj) {
                        avi.hide();
                        isLoading=false;
                        if (obj == null) {
                            Toast.makeText(FaceLoginActivity.this, "服务不可用，请检查网络", Toast.LENGTH_LONG).show();
                            return;
                        }
                        LogUtils.z("Login Response: " + obj.toString());
                        if (getJsonInt(obj, "errnum") == 0) {

                            if (type == FaceLoginActivity.TYPE_REGISTER) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(FaceLoginActivity.this);
                                builder.setMessage("人脸注册成功,请重新登录");
                                builder.setTitle("提示");
                                builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        FaceLoginActivity.this.finish();
                                    }
                                });
                                builder.create().show();
                            } else {
                                sp.edit().putInt("userId", getJsonInt(obj, "userId"))
                                        .putString("sessionId", getJsonString(obj, "sessionId")).apply();
                                startActivity(new Intent(FaceLoginActivity.this, MainActivity.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(FaceLoginActivity.this, getJsonString(obj, "errmsg"), Toast.LENGTH_LONG).show();
                        }
                    }
                }));
    }

    /**
     * 获取 IP 地址字符串
     *
     * @return
     */
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("sort", ex.toString());
        }
        return null;
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static String encodeBase64File(byte[] buffer) throws Exception {
        return Base64.encodeToString(buffer, Base64.NO_WRAP);
    }

    //防止内存溢出
    private void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    private void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @Override
    protected void onDestroy() {
        unSubscribe();
        super.onDestroy();
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
}
