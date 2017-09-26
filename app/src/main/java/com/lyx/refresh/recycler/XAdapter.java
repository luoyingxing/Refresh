package com.lyx.refresh.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * XAdapter
 * <p>
 * Created by luoyingxing on 2017/9/25.
 */

public abstract class XAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private Context mContext;
    private int mLayoutId;
    private List<T> mList;

    private OnItemClickListeners<T> mItemClickListener;

    public abstract void convert(ViewHolder holder, T item);

    public void add(T item) {
        mList.add(getItemCount(), item);
        notifyItemInserted(getItemCount());
    }

    public void addAll(List<T> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public XAdapter(Context context, List<T> list, int layoutId) {
        mContext = context;
        mLayoutId = layoutId;
        mList = list == null ? new ArrayList<T>() : list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.create(mContext, mLayoutId, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindItem(holder, position);
    }

    private void bindItem(final ViewHolder holder, final int position) {
        convert(holder, mList.get(position));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mItemClickListener) {
                    mItemClickListener.onItemClick(holder, mList.get(position), position);
                }
            }
        });
        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mItemClickListener) {
                    mItemClickListener.onItemLongClick(holder, mList.get(position), position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFooterView(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooterView(position)) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    public T getItem(int position) {
        if (mList.isEmpty()) {
            return null;
        }
        return mList.get(position);
    }

    private boolean isFooterView(int position) {
        return position >= getItemCount() - 1;
    }

    public void setOnItemClickListener(OnItemClickListeners<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListeners<T> {
        void onItemClick(ViewHolder holder, T item, int position);

        void onItemLongClick(ViewHolder holder, T item, int position);
    }


}