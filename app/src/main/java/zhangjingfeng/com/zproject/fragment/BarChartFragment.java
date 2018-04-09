package zhangjingfeng.com.zproject.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import zhangjingfeng.com.zproject.R;
import zhangjingfeng.com.zproject.utils.ConstantUtils;

/**
 * Created by Administrator on 2017/11/13.
 */

public class BarChartFragment extends Fragment {
    private TextView textView;
    private BarChart barChart;

    public static BarChartFragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.KEY_ARGS, s);
        BarChartFragment chartFragment = new BarChartFragment();
        chartFragment.setArguments(bundle);
        return chartFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart_layout, container, false);
        textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(getArguments().getString(ConstantUtils.KEY_ARGS));
        barChart = (BarChart) view.findViewById(R.id.bar_chart);
        barChart.setVisibility(View.VISIBLE);

        initStyle();
        initData();
        return view;
    }

    private void initStyle() {
        Description description = new Description();
        description.setText("柱状图");
        description.setTextColor(Color.parseColor("#FF4081"));
        barChart.setDescription(description);
        barChart.setNoDataText("没有数据");
        barChart.animateXY(1500, 1500);
//        barChart.setScaleEnabled(false);//禁止缩放

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new MyXAxisValueFormatter());

        YAxis lyAxis = barChart.getAxisLeft();
        lyAxis.setDrawGridLines(false);

        YAxis rYAxis = barChart.getAxisRight();
        rYAxis.setEnabled(false);

    }

    private void initData() {
        List<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        BarDataSet barDataSet = new BarDataSet(entries, "测试1");
        barDataSet.setColor(Color.parseColor("#99CCCC"));
        barDataSet.setValueTextSize(9f);
        barDataSet.setHighlightEnabled(false);

        List<BarEntry> entries1 = new ArrayList<BarEntry>();
        entries1.add(new BarEntry(0f, 20f));
        entries1.add(new BarEntry(1f, 20f));
        entries1.add(new BarEntry(2f, 30f));
        entries1.add(new BarEntry(3f, 40f));
        BarDataSet barDataSet1 = new BarDataSet(entries1, "测试2");
        barDataSet1.setColor(Color.parseColor("#FFCCCC"));
        barDataSet1.setValueTextSize(9f);
//        barDataSet1.setHighlightEnabled(false);

        BarData barData = new BarData(barDataSet, barDataSet1);
        barData.setHighlightEnabled(true);
        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
        barData.setBarWidth(barWidth); // set the width of each bar
        barChart.getXAxis().setAxisMinimum(0f);
        barChart.getXAxis().setAxisMaximum(4f);
        barChart.setData(barData);
        barChart.groupBars(0f,groupSpace,barSpace);
        barChart.setFitBars(true);//使x轴完全适合所有bar
        barChart.invalidate();

    }

    class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues = new String[]{"第一季度", "第二季度", "第三季度", "第四季度"};
        private DecimalFormat mFormat;

        public MyXAxisValueFormatter() {
//            mFormat = new DecimalFormat("###,###,###,##0.0");十进制格式 #代表没有则补空 0代表没有则补零
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
//            Log.d("", "getFormattedValue: " + value);
            if (value>=0&&value<=3){
                return  mValues[(int) value];
            }
            return "";
        }
    }
}
