package zhangjingfeng.com.zproject.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import zhangjingfeng.com.zproject.MyApplication;
import zhangjingfeng.com.zproject.R;
import zhangjingfeng.com.zproject.bean.ContactSortModel;
import zhangjingfeng.com.zproject.fragment.base.BaseFragment;
import zhangjingfeng.com.zproject.utils.ConstantUtils;
import zhangjingfeng.com.zproject.utils.PinyinComparator;
import zhangjingfeng.com.zproject.utils.PinyinUtils;
import zhangjingfeng.com.zproject.view.SideBar;

/**
 * Created by Administrator on 2018/1/19.
 */

public class TongXunLuFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private TextView tag, title_name;
    private LinearLayout title;
    private EditText search;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TongXunLuAdapter tongXunLuAdapter;
    private int currentTopItemPostion = 0;
    private int topHeight = 0;
    private SideBar sideBar;
    private List<ContactSortModel> list = new ArrayList<ContactSortModel>();

    public static TongXunLuFragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.KEY_ARGS, s);
        TongXunLuFragment tongXunLuFragment = new TongXunLuFragment();
        tongXunLuFragment.setArguments(bundle);
        return tongXunLuFragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_tong_xun_lu;
    }

    @Override
    public void initView(View view) {
        search=(EditText)view.findViewById(R.id.search_et);
        title = (LinearLayout) view.findViewById(R.id.title);
        title_name = (TextView) view.findViewById(R.id.title_name);
        tag = (TextView) view.findViewById(R.id.tag);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_view);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

        tongXunLuAdapter = new TongXunLuAdapter(list);
        recyclerView.setAdapter(tongXunLuAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                topHeight = title.getHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (currentTopItemPostion + 1 == tongXunLuAdapter.getPositionForSection(tongXunLuAdapter.getSectionForPosition(currentTopItemPostion + 1))) {
                    //获取当前顶部ItemView
                    View view = linearLayoutManager.findViewByPosition(currentTopItemPostion + 1);
                    if (view != null) {
                        if (view.getTop() <= topHeight) {
                            title.setY(-(topHeight - view.getTop()));//向上偏移
                        } else {
                            title.setY(0);
                        }
                    }
                }
                //更新currentTopItemPostion
                if (currentTopItemPostion != linearLayoutManager.findFirstVisibleItemPosition()) {
                    currentTopItemPostion = linearLayoutManager.findFirstVisibleItemPosition();
                    title.setY(0);
                    updateTitle();
                }
                sideBar.changeChoose(list.get(currentTopItemPostion).getSortLetters());

            }
        });
        sideBar = (SideBar) view.findViewById(R.id.sidebar);
        sideBar.setTextView(tag);//滑动时显示的TextView
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = tongXunLuAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    recyclerView.smoothScrollToPosition(position);
                }
            }
        });

    }

    //更新顶部Tile
    private void updateTitle() {
        title_name.setText(list.get(currentTopItemPostion).getSortLetters());
    }

    @Override
    public void initData() {

        getPhoneContacts("");
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 可以按联系人名查询
     *
     * @param name
     */
    private void getPhoneContacts(final String name) {
        ContentResolver resolver = MyApplication.APPContext.getContentResolver();
        //获取手机联系人
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "=?"
                + " AND " + ContactsContract.CommonDataKinds.Phone.TYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE + "'";

        String selection1 = ContactsContract.CommonDataKinds.Phone.TYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE + "'";

        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , projection, selection, new String[]{name}, null);
        Cursor cursor1 = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , projection, selection1, null, null);

//        if (cursor.moveToNext()) {
//            String number = cursor.getString(2);
//            Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
//        }
        while (cursor1.moveToNext()) {
            list.add(new ContactSortModel(cursor1.getString(1), "A"));
        }

        List<ContactSortModel> mSortList = filledData(list);
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        search.setHint("搜索"+mSortList.size()+"位联系人");
        tongXunLuAdapter.updateData(mSortList);
        cursor.close();
        cursor1.close();
    }


    /**
     * 设置首字母属性
     *
     * @param date
     * @return
     */
    private List<ContactSortModel> filledData(List<ContactSortModel> date) {
        List<ContactSortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.size(); i++) {
            ContactSortModel sortModel = new ContactSortModel();
            sortModel.setName(date.get(i).getName());
            String pinyin = PinyinUtils.getPingYin(date.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            } else {
                sortModel.setSortLetters("#");
                if (!indexString.contains("#")) {
                    indexString.add("#");
                }
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
                if (s.equals("@")
                        || t1.equals("#")) {
                    return -1;
                } else if (s.equals("#")
                        || t1.equals("@")) {
                    return 1;
                } else {
                    return s.compareTo(t1);
                }
            }
        });
        sideBar.setIndexText(indexString);
        return mSortList;
    }

    /**
     *
     */
    private class TongXunLuAdapter extends RecyclerView.Adapter<TongXunLuAdapter.myViewHolder> {
        private List<ContactSortModel> mapList;

        public TongXunLuAdapter(List<ContactSortModel> mapList) {
            this.mapList = mapList;
        }

        @Override
        public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_tongxunlu, parent, false);
            return new myViewHolder(view);
        }

        @Override
        public void onBindViewHolder(myViewHolder holder, int position) {
            int section = getSectionForPosition(position);
            if (position == getPositionForSection(section)) {
                holder.itemTile.setVisibility(View.VISIBLE);
                holder.itemTileName.setText(mapList.get(position).getSortLetters());
            } else {
                holder.itemTile.setVisibility(View.GONE);
            }
            holder.itemContent.setText(mapList.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return mapList != null ? mapList.size() : 0;
        }

        public class myViewHolder extends RecyclerView.ViewHolder {
            private TextView itemTileName;
            private TextView itemContent;
            private LinearLayout itemTile;

            public myViewHolder(View itemView) {
                super(itemView);
                itemTileName = (TextView) itemView.findViewById(R.id.item_title_name);
                itemContent = (TextView) itemView.findViewById(R.id.item_content);
                itemTile = (LinearLayout) itemView.findViewById(R.id.item_title);
            }
        }

        //返回首字母
        public int getSectionForPosition(int position) {
            return mapList.get(position).getSortLetters().charAt(0);
        }

        //用首字母查该字母首次出现的位置
        public int getPositionForSection(int section) {
            for (int i = 0; i < getItemCount(); i++) {
                String sortStr = mapList.get(i).getSortLetters();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * 当ListView数据发生变化时,调用此方法来更新ListView
         *
         * @param list
         */
        public void updateData(List<ContactSortModel> list) {
            mapList.clear();
            mapList.addAll(list);
            notifyDataSetChanged();
        }

    }
}
