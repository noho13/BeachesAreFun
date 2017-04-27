package com.normanhoeller.beachesarefun.beaches.adapter;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.normanhoeller.beachesarefun.R;
import com.normanhoeller.beachesarefun.beaches.Beach;
import com.normanhoeller.beachesarefun.beaches.CacheWrapper;
import com.normanhoeller.beachesarefun.databinding.ItemBeachBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by norman on 22/04/17.
 */

public class BeachAdapter extends RecyclerView.Adapter<BeachAdapter.ViewHolder> {

    private List<Beach> beachList = new ArrayList<>();
    private CacheWrapper cacheWrapper;

    public BeachAdapter(CacheWrapper cacheWrapper) {
        this.cacheWrapper = cacheWrapper;
    }

    public void setBeachList(List<Beach> beaches) {
        int insertedAt = beachList.size();
        beachList.addAll(beaches);

        notifyItemInserted(insertedAt);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemBeachBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_beach, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Beach item = beachList.get(position);
        holder.itemBeachBinding.setBeach(item);
        holder.itemBeachBinding.setCacheWrapper(cacheWrapper);
        holder.itemBeachBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (beachList != null) {
            return beachList.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemBeachBinding itemBeachBinding;

        public ViewHolder(ItemBeachBinding itemBeachBinding) {
            super(itemBeachBinding.getRoot());
            this.itemBeachBinding = itemBeachBinding;
        }
    }
}
