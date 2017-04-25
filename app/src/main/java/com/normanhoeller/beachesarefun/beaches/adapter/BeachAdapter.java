package com.normanhoeller.beachesarefun.beaches.adapter;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.normanhoeller.beachesarefun.R;
import com.normanhoeller.beachesarefun.beaches.BeachModel;
import com.normanhoeller.beachesarefun.beaches.CacheWrapper;
import com.normanhoeller.beachesarefun.databinding.ItemBeachBinding;

import java.util.List;

/**
 * Created by norman on 22/04/17.
 */

public class BeachAdapter extends RecyclerView.Adapter<BeachAdapter.ViewHolder> {

    private List<BeachModel> beachModelList;
    private CacheWrapper cacheWrapper;

    public BeachAdapter(LruCache<String, Bitmap> memCache, int spanWidth) {
        this.cacheWrapper = new CacheWrapper(memCache, spanWidth);
    }

    public void setBeachModelList(List<BeachModel> beaches) {
        if (beachModelList != null) {
            beachModelList.addAll(beaches);
        } else {
            beachModelList = beaches;
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemBeachBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_beach, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BeachModel item = beachModelList.get(position);
        holder.itemBeachBinding.setBeach(item);
        holder.itemBeachBinding.setCacheWrapper(cacheWrapper);
        holder.itemBeachBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (beachModelList != null) {
            return beachModelList.size();
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
