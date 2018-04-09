package zhangjingfeng.com.zproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import zhangjingfeng.com.zproject.MyApplication;
import zhangjingfeng.com.zproject.R;
import zhangjingfeng.com.zproject.activity.FaceLoginActivity;
import zhangjingfeng.com.zproject.activity.MPAndroidChartActivity;
import zhangjingfeng.com.zproject.bean.MyMap;
import zhangjingfeng.com.zproject.fragment.base.BaseFragment;
import zhangjingfeng.com.zproject.utils.ConstantUtils;
import zhangjingfeng.com.zproject.view.MyGridView;
import zhangjingfeng.com.zproject.view.TextSwitcherView;


public class GuanLiFragment extends BaseFragment {
    private MyGridView guanLiGridView, qiTaGridView;
    private TextSwitcherView textSwitcherView;


    public static GuanLiFragment newInstance(String s) {
        GuanLiFragment fragment = new GuanLiFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.KEY_ARGS, s);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_guan_li;
    }

    @Override
    public void initView(View view) {
        guanLiGridView = (MyGridView) view.findViewById(R.id.guanli_grid_view);
        qiTaGridView = (MyGridView) view.findViewById(R.id.qita_grid_view);
        textSwitcherView=(TextSwitcherView)view.findViewById(R.id.text_switcher_view);
    }

    @Override
    public void initData() {
        List<MyMap> myMapList = new ArrayList<MyMap>();
        myMapList.add(new MyMap(R.mipmap.chaxun, "查询"));
        myMapList.add(new MyMap(R.mipmap.lishibaogao, "报表"));
        myMapList.add(new MyMap(R.mipmap.shenqing, "申请"));
        myMapList.add(new MyMap(R.mipmap.shenpi, "审批"));
        myMapList.add(new MyMap(R.mipmap.xiugaishenpi, "修改审批"));
        myMapList.add(new MyMap(R.mipmap.tubiao, "图标统计"));
        guanLiGridView.setAdapter(new MyGridAdapter(myMapList));
        guanLiGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                switch (i){
                    case 5:
                        intent.setClass(getActivity(), MPAndroidChartActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        List<MyMap> myMapList1 = new ArrayList<MyMap>();
        myMapList1.add(new MyMap(R.mipmap.api, "API"));
        myMapList1.add(new MyMap(R.mipmap.baocun, "保存"));
        myMapList1.add(new MyMap(R.mipmap.kongzhimianban, "控制面板"));
        qiTaGridView.setAdapter(new MyGridAdapter(myMapList1));
        qiTaGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                switch (i){
                    case 0:
                        intent.setClass(getActivity(), FaceLoginActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        textSwitcherView.setTextResources(new String[]{"133","222","333","4444"});
        textSwitcherView.setStillTime(3000l);
        super.onResume();
    }

    @Override
    public void onPause() {
        textSwitcherView.cancelStillTime();
        super.onPause();
    }

    private class MyGridAdapter extends BaseAdapter {
        private List<MyMap> myMapList;

        public MyGridAdapter(List<MyMap> myMapList) {
            this.myMapList = myMapList;
        }

        @Override
        public int getCount() {
            return myMapList != null ? myMapList.size() : 0;
        }

        @Override
        public Object getItem(int i) {
            return myMapList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid, viewGroup, false);
                viewHolder.icon = (ImageView) view.findViewById(R.id.grid_item_icon);
                viewHolder.text = (TextView) view.findViewById(R.id.grid_item_text);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            Glide.with(MyApplication.APPContext).load(myMapList.get(i).getKey()).into(viewHolder.icon);
            viewHolder.text.setText(myMapList.get(i).getValue());
            return view;
        }

        public class ViewHolder {
            ImageView icon;
            TextView text;
        }
    }
}
