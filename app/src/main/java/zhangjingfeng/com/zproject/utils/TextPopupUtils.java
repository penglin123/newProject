package zhangjingfeng.com.zproject.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import zhangjingfeng.com.zproject.R;

/**
 * Created by Administrator on 2018/1/13.
 */

public class TextPopupUtils {
    private Context context;
    private String str;
    private PopupWindow popupWindow;
    public static TextPopupUtils instance;

    //获取单例对象，线程安全
    public static TextPopupUtils getInstance(Context context,String s){
        if(instance==null){
            synchronized (TextPopupUtils.class){
                if(instance==null){
                    instance=new TextPopupUtils(context,s);
                }
            }
        }
        return instance;
    }

    public TextPopupUtils(Context context,String str ) {
        this.context=context;
        this.str=str;
        initPop();
    }

    private void initPop() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_refresh, null);
        ((TextView)contentView.findViewById(R.id.str_tv)).setText(str);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置可以获得焦点
        popupWindow.setFocusable(false);
        //设置弹窗内可点击
        popupWindow.setTouchable(false);
        //设置弹窗外可点击
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.MyPopAnimator);
        //
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }
    public void show(View view){
        popupWindow.showAsDropDown(view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(popupWindow!=null) {
                    popupWindow.dismiss();
                }
            }
        }, 1000);
    }
}
