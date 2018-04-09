package zhangjingfeng.com.zproject.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import zhangjingfeng.com.zproject.R;
import zhangjingfeng.com.zproject.utils.ConstantUtils;

/**
 * Created by Administrator on 2017/11/13.
 */

public class PieChartFragment extends Fragment {
    private PieChart pieChart;

    public static PieChartFragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.KEY_ARGS, s);
        PieChartFragment chartFragment = new PieChartFragment();
        chartFragment.setArguments(bundle);
        return chartFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart_layout, container, false);
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(getArguments().getString(ConstantUtils.KEY_ARGS));

        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        pieChart.setVisibility(View.VISIBLE);

        initStyle();
        initData();
        return view;
    }

    private void initStyle() {
        Description description = new Description();
        description.setText("测试圆饼图");
        pieChart.setDescription(description);
        pieChart.setNoDataText("没有数据");
        pieChart.animateXY(1500, 1500);
        pieChart.setCenterText("中心");
        pieChart.setUsePercentValues(true);
//        pieChart.setCenterTextRadiusPercent(0.8f);

        pieChart.setExtraOffsets(10.f, 0.f, 10.f, 0.f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(38f);
        pieChart.setTransparentCircleRadius(41f);
    }

    private void initData() {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(18.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));

        PieDataSet pieDataSet = new PieDataSet(entries, "颜色");
        pieDataSet.setColors(new int[]{
                Color.parseColor("#99CCCC"),
                Color.parseColor("#FFCCCC"),
                Color.parseColor("#CCCC99"),
                Color.parseColor("#FFCC99"),
                Color.parseColor("#CCCCFF"),});
        pieDataSet.setValueTextSize(11f);
        pieDataSet.setValueTextColor(Color.parseColor("#ffffff"));
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieChart.setData(pieData);
        pieChart.invalidate();

    }
}
