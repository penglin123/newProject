package zhangjingfeng.com.zproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zhangjingfeng.com.zproject.R;
import zhangjingfeng.com.zproject.utils.ConstantUtils;


/**
 * Created by Administrator on 2017/11/13.
 */

public class RadarChartFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chart_layout,container,false);
        TextView textView= (TextView) view.findViewById(R.id.text_view);
        textView.setText(getArguments().getString(ConstantUtils.KEY_ARGS));
        return view;
    }
    public static RadarChartFragment newInstance(String s){
        Bundle bundle=new Bundle();
        bundle.putString(ConstantUtils.KEY_ARGS,s);
        RadarChartFragment chartFragment=new RadarChartFragment();
        chartFragment.setArguments(bundle);
        return chartFragment;
    }
}
