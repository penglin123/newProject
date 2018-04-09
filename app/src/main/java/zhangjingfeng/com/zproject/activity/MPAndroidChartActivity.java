package zhangjingfeng.com.zproject.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import zhangjingfeng.com.zproject.R;
import zhangjingfeng.com.zproject.activity.base.BaseActivity;
import zhangjingfeng.com.zproject.fragment.BarChartFragment;
import zhangjingfeng.com.zproject.fragment.LineChartFragment;
import zhangjingfeng.com.zproject.fragment.PieChartFragment;
import zhangjingfeng.com.zproject.fragment.RadarChartFragment;

public class MPAndroidChartActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    private BottomNavigationBar mBottomNavigationBar;
    private LineChartFragment lineChartFragment;
    private BarChartFragment barChartFragment;
    private PieChartFragment pieChartFragment;
    private RadarChartFragment radarChartFragment;


    @Override
    public int getLayoutId() {
        return R.layout.activity_mpandroid_chart;
    }

    @Override
    public void initData() {
        setToolbarTitle("图表");
    }

    @Override
    public void initView() {
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.home_no, "折线").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.home_shenpi_no, "柱状").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.home_che_no, "圆饼").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.home_my_no, "雷达").setActiveColorResource(R.color.colorPrimary))
                .setFirstSelectedPosition(0)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();
        
    }

    private void setDefaultFragment() {
        lineChartFragment= LineChartFragment.newInstance("折线");
        getSupportFragmentManager().beginTransaction().replace(R.id.content_ll,lineChartFragment).commit();
        setToolbarTitle("折线");

    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        switch(position){
            case 0:
                if(lineChartFragment==null){
                    lineChartFragment=LineChartFragment.newInstance("折线");
                }
                fragmentTransaction.replace(R.id.content_ll,lineChartFragment);
                setToolbarTitle("折线");
                break;
            case 1:
                if(barChartFragment==null){
                    barChartFragment=BarChartFragment.newInstance("柱状");
                }
                fragmentTransaction.replace(R.id.content_ll,barChartFragment);
                setToolbarTitle("柱状");

                break;
            case 2:
                if(pieChartFragment==null){
                    pieChartFragment=PieChartFragment.newInstance("圆饼");
                }
                fragmentTransaction.replace(R.id.content_ll,pieChartFragment);
                setToolbarTitle("圆饼");
                break;
            case 3:
                if(radarChartFragment==null){
                    radarChartFragment=RadarChartFragment.newInstance("雷达");
                }
                fragmentTransaction.replace(R.id.content_ll,radarChartFragment);
                setToolbarTitle("雷达");
                break;
            default:
                break;
        }
        fragmentTransaction.commit();

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
