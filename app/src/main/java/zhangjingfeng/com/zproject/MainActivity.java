package zhangjingfeng.com.zproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.menu.MenuBuilder;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zhangjingfeng.com.zproject.activity.LoginActivity;
import zhangjingfeng.com.zproject.activity.base.BaseActivity;
import zhangjingfeng.com.zproject.bean.jsonbean.UpdateAppInfoBean;
import zhangjingfeng.com.zproject.fragment.GongWenFragment;
import zhangjingfeng.com.zproject.fragment.GuanLiFragment;
import zhangjingfeng.com.zproject.fragment.HomeFragment;
import zhangjingfeng.com.zproject.fragment.TongXunLuFragment;
import zhangjingfeng.com.zproject.utils.APPUpdateUtils;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    private BottomNavigationBar mBottomNavigationBar;
    private BadgeItem badgeItem;
    private long lastDate = 0;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private String[] titles = {"资讯", "公文", "管理", "通讯录"};
    private SharedPreferences sp;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        //顶部动作栏，替换图标，默认为箭头
        getToolbar().setNavigationIcon(R.mipmap.icon);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });
        setToolbarTitle("主页");

        //小圆点
        badgeItem = new BadgeItem();
        badgeItem.setHideOnSelect(true)
                .setText("11")
                .setBackgroundColorResource(R.color.colorAccent)
                .setBorderWidth(1);
        //导航栏
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.home_no, "资讯").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.home_shenpi_no, "公文").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.home_che_no, "管理").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.home_my_no, "通讯录").setActiveColorResource(R.color.colorPrimary).setBadgeItem(badgeItem))
                .setFirstSelectedPosition(0)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(this);
        //中间布局
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new HomeFragment().newInstance(titles[0]));
        fragmentList.add(new GongWenFragment().newInstance(titles[1]));
        fragmentList.add(new GuanLiFragment().newInstance(titles[2]));
        fragmentList.add(new TongXunLuFragment().newInstance(titles[3]));
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList, titles));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationBar.selectTab(position, false);//不触发BottomNavigationBar selected监听事件
                setToolbarTitle(titles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {
        sp=getSharedPreferences("config",MODE_PRIVATE);
        checkUpdate(APPUpdateUtils.getLocalVersion(MyApplication.APPContext));
    }

    /**
     * 更新检查
     *
     * @param curVersion 当前版本号
     */
    private void checkUpdate(double curVersion) {
        addSubscribe(MyApplication.apiService.getUpdateInfo()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateAppInfoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UpdateAppInfoBean updateAppInfo) {
                        // TODO 2018/2/8
                        //版本判断
                        showUpdate(updateAppInfo);
                    }
                }));

    }

    /**
     * 更新提示框
     *
     * @param updateAppInfo 服务器最新数据
     */
    private void showUpdate(UpdateAppInfoBean updateAppInfo) {
        AlertDialog mDialog = new AlertDialog.Builder(mContext)
                .setTitle("发现新版本")
                .setMessage("木有更新日志..")
                .setCancelable(false)
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        APPUpdateUtils.downLoadApk(mContext,"","");
                    }
                })
                .setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        mDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        //通过反射让图标显示
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                //日夜主题却换
                if(sp.getBoolean("isNight",false)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sp.edit().putBoolean("isNight",false).apply();
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sp.edit().putBoolean("isNight",true).apply();
                }
                recreate();
                break;
            case R.id.update:
                showUpdate(new UpdateAppInfoBean());
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(int position) {
        setToolbarTitle(titles[position]);
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onBackPressed() {
        if ((new Date()).getTime() - lastDate < 2000) {
            finish();
        } else {
            lastDate = new Date().getTime();
            showToast("再按一次退出");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_MENU){
            //拦截菜单键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;
        private String[] titles;

        public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titles) {
            super(fm);
            this.fragmentList = fragmentList;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.titles[position];
        }
    }
}
