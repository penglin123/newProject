package zhangjingfeng.com.zproject.utils;

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

import java.util.List;

import zhangjingfeng.com.zproject.R;

/**
 * 自定义的popupWindow 有背景虚化
 * Created by Administrator on 2018/1/11.
 */

public class ListPopupUtils {
    private Context context;
    private View view;
    private ListView listView;
    private List<String> list;
    private PopupWindow popupWindow;

    public ListPopupUtils(Context context, View view, List<String> list) {
        this.context = context;
        this.view = view;
        this.list = list;
        initPop();
    }

    private void initPop() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_custom, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置可以获得焦点
        popupWindow.setFocusable(true);
        //设置弹窗内可点击
        popupWindow.setTouchable(true);
        //设置弹窗外可点击
        popupWindow.setOutsideTouchable(true);
        //
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //透明度
        setBackgroundAlpha(0.8f);
        //pop取消监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
//        setAnimationStyle(R.style.PopupAnimation);
        initData(contentView);
    }

    /***
     * 设置添加屏幕的背景透明度* @param bgAlpha
     */
    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams layoutParams = ((Activity) context).getWindow().getAttributes();
        layoutParams.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(layoutParams);
    }

    private void initData(View contentView) {
        listView = (ListView) contentView.findViewById(R.id.title_list);
        //设置列表的适配器
        listView.setAdapter(new MyListAdapter());
    }

    public void show() {
        popupWindow.showAsDropDown(view);
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
