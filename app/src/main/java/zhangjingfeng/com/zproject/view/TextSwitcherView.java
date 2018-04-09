package zhangjingfeng.com.zproject.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.Timer;
import java.util.TimerTask;

import zhangjingfeng.com.zproject.R;
import zhangjingfeng.com.zproject.utils.LogUtils;

/**
 * Created by Administrator on 2018/1/30.
 */

public class TextSwitcherView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private Context context;
    private Timer timer;
    private String[] resources={"1","2","3","4"};
    private int index = -1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    index = next();
                    updateText();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private int next() {
        int flag = index + 1;
        if (flag > resources.length - 1) {
            flag = flag - resources.length;
        }
        return flag;
    }

    public TextSwitcherView(Context context) {
        super(context);
        this.context = context;
    }

    public TextSwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        this.setInAnimation(context, R.anim.in_animation);
        this.setOutAnimation(context, R.anim.out_animation);
        this.setFactory(this);
    }

    @Override
    public View makeView() {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    public void updateText() {
        this.setText(resources[index]);
    }

    /**
     * 设置数据
     *
     * @param resources
     */
    public void setTextResources(String[] resources) {
        this.resources = resources;
    }

    /**
     * 设置定时器间隔时间
     *
     * @param time
     */
    public void setStillTime(Long time) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new MyTask(), 1, time);
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            handler.obtainMessage(1).sendToTarget();
        }
    }

    /**
     * 取消定时器
     */
    public void cancelStillTime(){
        if (timer!=null){
            timer.cancel();
        }
    }
}