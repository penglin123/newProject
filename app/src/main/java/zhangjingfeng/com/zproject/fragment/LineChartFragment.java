package zhangjingfeng.com.zproject.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import zhangjingfeng.com.zproject.R;
import zhangjingfeng.com.zproject.utils.ConstantUtils;

/**
 * Created by Administrator on 2017/11/13.
 */

public class LineChartFragment extends Fragment {
    private TextView textView;
    private LineChart lineChart;

    public static LineChartFragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.KEY_ARGS, s);
        LineChartFragment chartFragment = new LineChartFragment();
        chartFragment.setArguments(bundle);
        return chartFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart_layout, container, false);
        textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(getArguments().getString(ConstantUtils.KEY_ARGS));
        lineChart = (LineChart) view.findViewById(R.id.line_chart);
        lineChart.setVisibility(View.VISIBLE);
        initStyle();
        initData();
        return view;
    }

    private void initStyle() {
        Description description = new Description();
        description.setText("测试折线图表");
        description.setTextColor(Color.parseColor("#FF4081"));
        lineChart.setDescription(description);
        lineChart.setNoDataText("没有数据");
        lineChart.animateXY(1500,1500);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        //刻度
        xAxis.setGranularity(1f);
//        xAxis.setValueFormatter(new MyXAxisValueFormatter(new String[]{"a", "b", "c", "d", "e", "f"}));
        YAxis lYAxis = lineChart.getAxisLeft();
        YAxis rYAxis = lineChart.getAxisRight();
        lYAxis.setDrawGridLines(true);
        rYAxis.setEnabled(false);
//        lYAxis.setValueFormatter(new MyYAxisValueFormatter());
    }

    private void initData() {
        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(0, 1));
        entries.add(new Entry(1, 3));
        entries.add(new Entry(2, 4));
        entries.add(new Entry(3, 5));
        entries.add(new Entry(4, 5));
        entries.add(new Entry(5, 5));

        LineDataSet lineDataSet = new LineDataSet(entries, "图1");
        lineDataSet.setColor(Color.parseColor("#FF4081"));//设置折线颜色
        lineDataSet.enableDashedLine(10f, 10f, 0f);//将折线设置为虚线
        lineDataSet.setLineWidth(2.5f);//折线宽度
        lineDataSet.setCircleRadius(4.5f);//圆圈半径
        lineDataSet.setValueTextSize(9f);//值大小

        List<Entry> entries2 = new ArrayList<Entry>();
        entries2.add(new Entry(0, 2));
        entries2.add(new Entry(1, 5));
        entries2.add(new Entry(2, 3));
        entries2.add(new Entry(3, 6));
        entries2.add(new Entry(4, 7));
        LineDataSet lineDataSet2 = new LineDataSet(entries2, "图2");
        lineDataSet2.setLineWidth(2.5f);//折线宽度
        lineDataSet2.setCircleRadius(4.5f);//圆圈半径
        lineDataSet2.setCircleColor(Color.parseColor("#FF4081"));
        lineDataSet2.setValueTextSize(9f);//值大小

        List<ILineDataSet> lineDataSets=new ArrayList<>();
        lineDataSets.add(lineDataSet);
        lineDataSets.add(lineDataSet2);

        LineData lineData = new LineData(lineDataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value) + " $";
        }

    }

    class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;
        private DecimalFormat mFormat;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
            mFormat = new DecimalFormat("###,###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mFormat.format(value);
        }
    }
}
