package br.com.tocalivros.mupdfsample.foobnix.ui2;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.tocalivros.mupdfsample.foobnix.android.utils.ResultResponse;

public abstract class AppRecycleAdapter<K, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    protected final List<K> items = new ArrayList<K>();

    public K getItem(int pos) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        if (items.size() - 1 >= pos) {
            return items.get(pos);
        } else {
            return items.get(0);
        }
    }

    public List<K> getItemsList() {
        return items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(ResultResponse<K> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(ResultResponse<K> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    protected ResultResponse<K> onItemClickListener;
    protected ResultResponse<K> onItemLongClickListener;

    public void bindItemClickAndLongClickListeners(View view, final K item) {
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onResultRecive(item);
                }
            }
        });
        view.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onResultRecive(item);
                }
                return true;
            }
        });
    }

}