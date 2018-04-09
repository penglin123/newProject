package zhangjingfeng.com.zproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import zhangjingfeng.com.zproject.R;
import zhangjingfeng.com.zproject.api.ApiService;
import zhangjingfeng.com.zproject.bean.jsonbean.BooksByCatsBean;

/**
 * Created by Administrator on 2017/11/1.
 */

public class BooksByCatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BooksByCatsBean.BooksBean> mList;
    private Context mContext;
    public static final int TYPE_ITEM = 0;//正常Item
    public static final int TYPE_FOOTER = 1;//加载中显示的View
    public static final int LOADING = 0;//加载中
    public static final int OVER = 1;//空闲状态
    private int status = OVER;
    private int count = 0;//当前加载个数
    private onMyClickListener listener;

    public BooksByCatsAdapter(Context context, List<BooksByCatsBean.BooksBean> list) {
        mList = new ArrayList<>();
        mContext = context;
        count = (list == null ? 0 : list.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new ItemHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_footer, parent, false);
            return new FooterHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else
            return TYPE_ITEM;
    }

    public void setData(List<BooksByCatsBean.BooksBean> data) {
        mList.clear();
        mList.addAll(data);
        count=data.size();
        notifyDataSetChanged();
    }

    public void addData(List<BooksByCatsBean.BooksBean> data) {

        mList.addAll(data);
        notifyItemInserted(count);//局部刷新
        count=mList.size();//当前已加载个数
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder itemHolder = (ItemHolder) holder;
            itemHolder.textView.setText(mList.get(position).getTitle());
            Glide.with(mContext).load(ApiService.IMG_BASE_URL + mList.get(position).getCover()).into(itemHolder.imageView);
            if(listener!=null) {
                itemHolder.dialogTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClickListener(position, view);
                    }
                });
            }
        } else if (holder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) holder;

            if (status == LOADING) {
                footerHolder.progressBar.setVisibility(View.VISIBLE);
                footerHolder.textView.setText("加载中...");
            } else if (status == OVER) {
                footerHolder.progressBar.setVisibility(View.GONE);
                footerHolder.textView.setText("上拉加载更多");
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 :(mList.size()==0?0:mList.size() + 1) ;
    }

    public void changeMoreStatus(int status) {
        this.status = status;
        notifyDataSetChanged();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private TextView dialogTv;

        public ItemHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.icon);
            textView = (TextView) itemView.findViewById(R.id.name);
            dialogTv=(TextView)itemView.findViewById(R.id.dialog_tv);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private TextView textView;
        private ProgressBar progressBar;

        public FooterHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.view_footer_ll);
            textView = (TextView) itemView.findViewById(R.id.load_tv);
            progressBar = (ProgressBar) itemView.findViewById(R.id.load_progress);

        }
    }
    public interface onMyClickListener{
        void onClickListener(int position, View view);
    }
    public void setMyClickListener(onMyClickListener listener){
        this.listener=listener;
    }

}
