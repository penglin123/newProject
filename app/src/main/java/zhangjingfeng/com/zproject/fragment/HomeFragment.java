package zhangjingfeng.com.zproject.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zhangjingfeng.com.zproject.R;
import zhangjingfeng.com.zproject.adapter.BooksByCatsAdapter;
import zhangjingfeng.com.zproject.bean.jsonbean.BooksByCatsBean;
import zhangjingfeng.com.zproject.fragment.base.BaseFragment;
import zhangjingfeng.com.zproject.utils.ConstantUtils;
import zhangjingfeng.com.zproject.utils.TextPopupUtils;
import zhangjingfeng.com.zproject.view.CustomPop;

import static zhangjingfeng.com.zproject.MyApplication.apiService;

/**
 * Created by Administrator on 2017/11/13.
 */

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private List<BooksByCatsBean.BooksBean> mlist;
    private static final String TAG = "BooksByCatsActivity";
    private int lastVisibleItem;
    private int limit = 20;
    private int start = 0;
    private BooksByCatsAdapter booksByCatsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private View topView;

    public static HomeFragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.KEY_ARGS, s);
        HomeFragment chartFragment = new HomeFragment();
        chartFragment.setArguments(bundle);
        return chartFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    public void initView(View view) {
        topView = view.findViewById(R.id.top_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        booksByCatsAdapter = new BooksByCatsAdapter(getActivity(), mlist);
        recyclerView.setAdapter(booksByCatsAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_view);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

        booksByCatsAdapter.setMyClickListener(new BooksByCatsAdapter.onMyClickListener() {
            @Override
            public void onClickListener(int position, View view) {
                Log.i(TAG, "onClickListener: 1");
                new CustomPop(getActivity()).showAsDropDown(view);
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (lastVisibleItem == recyclerView.getAdapter().getItemCount() - 1 &&
                        recyclerView.getAdapter().getItemCount() > limit - 1) {
                    booksByCatsAdapter.changeMoreStatus(BooksByCatsAdapter.LOADING);
                    start = booksByCatsAdapter.getItemCount();
                    getBooksByCats();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager instanceof LinearLayoutManager) {
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                }
            }
        });
    }

    public void initData() {
        getBooksByCats();
    }

    @Override
    public void onRefresh() {
        start = 0;
        getBooksByCats();
    }

    public void getBooksByCats() {
        Subscription subscription = apiService.getBooksByCats("male", "hot", "玄幻", "东方玄幻", start, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BooksByCatsBean>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(BooksByCatsBean booksByCats) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d(TAG, "onNext: " + new Gson().toJson(booksByCats));
                        mlist = new ArrayList<BooksByCatsBean.BooksBean>();
                        mlist = booksByCats.getBooks();
                        if (mlist != null && mlist.size() > 0) {
                            if (start == 0) {
                                TextPopupUtils.getInstance(getContext(), "刷新完成").show(topView);
                                booksByCatsAdapter.setData(mlist);
                            } else {
                                booksByCatsAdapter.addData(mlist);
                            }
                        }
                    }
                });
        addSubscribe(subscription);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
