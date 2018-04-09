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
import zhangjingfeng.com.zproject.fragment.base.ScrollAbleFragment;
import zhangjingfeng.com.zproject.utils.ConstantUtils;

import static zhangjingfeng.com.zproject.MyApplication.apiService;

/**
 * Created by Administrator on 2018-01-15.
 */

public class RecyclerFragment extends ScrollAbleFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "RecyclerFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private BooksByCatsAdapter booksByCatsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<BooksByCatsBean.BooksBean> mList ;
    private int lastVisibleItem = 0;
    private int limit = 20;//单页Item最大个数
    private int start = 0;
    private String tag;

    public static RecyclerFragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.KEY_ARGS, s);
        RecyclerFragment recyclerFragment = new RecyclerFragment();
        recyclerFragment.setArguments(bundle);
        return recyclerFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    public void initView(View view) {
        tag=getArguments().getString(ConstantUtils.KEY_ARGS);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_view);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        booksByCatsAdapter = new BooksByCatsAdapter(getActivity(), mList);
        recyclerView.setAdapter(booksByCatsAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (lastVisibleItem == recyclerView.getAdapter().getItemCount() - 1 &&
                        recyclerView.getAdapter().getItemCount() >= limit) {
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

    @Override
    public void initData() {
        getBooksByCats();
    }

    @Override
    public void onRefresh() {
        start = 0;
        getBooksByCats();
        Log.d(TAG, "onRefresh: ");
    }

    public void getBooksByCats() {
        Subscription subscription = apiService.getBooksByCats("male", "hot", "玄幻", "东方玄幻", start, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BooksByCatsBean>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onNext(BooksByCatsBean booksByCats) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d(TAG, tag+"  -  onNext: " + new Gson().toJson(booksByCats));
                        mList = new ArrayList<BooksByCatsBean.BooksBean>();
                        mList = booksByCats.getBooks();
                        if (mList != null && mList.size() > 0) {
                            if (start == 0) {
                                booksByCatsAdapter.setData(mList);
                            } else {
                                booksByCatsAdapter.addData(mList);
                            }
                        }
                    }
                });
        addSubscribe(subscription);
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

    //使用scrollableLayout 必须实现此方法，返回滑动布局
    @Override
    public View getScrollableView() {
        return recyclerView;
    }
}
