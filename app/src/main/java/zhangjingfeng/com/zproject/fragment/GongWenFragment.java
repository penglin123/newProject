package zhangjingfeng.com.zproject.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zhangjingfeng.com.zproject.R;
import zhangjingfeng.com.zproject.fragment.base.BaseFragment;
import zhangjingfeng.com.zproject.fragment.base.ScrollAbleFragment;
import zhangjingfeng.com.zproject.loader.GlideImageLoader;
import zhangjingfeng.com.zproject.utils.ConstantUtils;
import zhangjingfeng.com.zproject.utils.LogUtils;
import zhangjingfeng.com.zproject.view.scrollable.ScrollableLayout;

/**
 * Created by Administrator on 2017/11/13.
 */

public class GongWenFragment extends BaseFragment implements OnBannerListener {
    private TextView textView;
    private ScrollableLayout mScrollLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<ScrollAbleFragment> fragmentList;
    private String[] titles = {"我的", "待办", "完成", "转交"};
    private Banner banner;
    private String[] urls = {"http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg",
            "http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg",
            "http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg",
            "http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg"};
    private List<String> texts=new ArrayList<>();

    public static GongWenFragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.KEY_ARGS, s);
        GongWenFragment chartFragment = new GongWenFragment();
        chartFragment.setArguments(bundle);
        return chartFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gong_wen;
    }

    @Override
    public void initView(View view) {
        banner = (Banner) view.findViewById(R.id.banner);
        tabLayout = (TabLayout) view.findViewById(R.id.gongwen_tab);
        // ScrollableLayout
        mScrollLayout = (ScrollableLayout) view.findViewById(R.id.sl_root);
        viewPager = (ViewPager) view.findViewById(R.id.gongwen_vp);
        tabLayout.setupWithViewPager(viewPager);

        fragmentList = new ArrayList<ScrollAbleFragment>();
        fragmentList.add(RecyclerFragment.newInstance("1"));
        fragmentList.add(RecyclerFragment.newInstance("2"));
        fragmentList.add(RecyclerFragment.newInstance("3"));
        fragmentList.add(RecyclerFragment.newInstance("4"));
        viewPager.setAdapter(new MyViewPagerAdapter(getChildFragmentManager(), fragmentList));
        mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(0));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                LogUtils.z("1111");
                mScrollLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {
        List<String> images = new ArrayList<>(Arrays.asList(urls));
        texts.addAll(Arrays.asList(titles));
        //简单使用
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImages(images)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .setBannerTitles(texts)
                .setDelayTime(3000)
                .start();
    }

    //轮播点击事件
    @Override
    public void OnBannerClick(int position) {

    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        private List<ScrollAbleFragment> fragmentList;

        public MyViewPagerAdapter(FragmentManager fm, List<ScrollAbleFragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public int getCount() {
            return fragmentList != null ? fragmentList.size() : 0;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
