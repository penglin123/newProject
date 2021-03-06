package zhangjingfeng.com.zproject.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import zhangjingfeng.com.zproject.R;

/**
 * 自定义的ListPopupWindow 无背景虚化
 * Created by Administrator on 2018/1/10.
 */
public class CustomPop extends PopupWindow {
    private Context context;
    private View view;
    private ListView listView;
    private List<String> list;

    public CustomPop(Context context) {
        this(context,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public CustomPop(Context context, int with, int height) {
        this.context = context;
        setWidth(with);
        setHeight(height);
        //设置可以获得焦点
        setFocusable(true);
        //设置弹窗内可点击
        setTouchable(true);
        //设置弹窗外可点击
        setOutsideTouchable(true);
        //
        setBackgroundDrawable(new BitmapDrawable());
        view = LayoutInflater.from(context).inflate(R.layout.popup_custom, null);
        setContentView(view);
//        setAnimationStyle(R.style.PopupAnimation);
        initData();
    }

    private void initData() {
        listView = (ListView) view.findViewById(R.id.title_list);
        list = new ArrayList<String>();
        list.add("添加好友");
        list.add("扫一扫");
        list.add("支付宝");
        list.add("视频聊天");
        //设置列表的适配器
        listView.setAdapter(new MyListAdapter());
    }
    /***
     * 设置添加屏幕的背景透明度* @param bgAlpha
     */
    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams layoutParams = ((Activity) context).getWindow().getAttributes();
        layoutParams.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(layoutParams);
    }


    public class MyListAdapter extends BaseAdapter {
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView textView = null;

            if (convertView == null) {
                textView = new TextView(context);
                textView.setTextColor(Color.rgb(255, 255, 255));
                textView.setTextSize(14);
                //设置文本居中
                textView.setGravity(Gravity.CENTER);
                //设置文本域的范围
                textView.setPadding(0, 13, 0, 13);
                //设置文本在一行内显示（不换行）
                textView.setSingleLine(true);
            } else {
                textView = (TextView) convertView;
            }
            //设置文本文字
            textView.setText(list.get(position));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, list.get(position), Toast.LENGTH_SHORT).show();
                }
            });
            //设置文字与图标的间隔
            textView.setCompoundDrawablePadding(10);
            //设置在文字的左边放一个图标
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.bofang);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            textView.setCompoundDrawables(drawable, null, null, null);
            return textView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
