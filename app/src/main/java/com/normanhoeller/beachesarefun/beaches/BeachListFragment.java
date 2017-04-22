package com.normanhoeller.beachesarefun.beaches;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.normanhoeller.beachesarefun.NetworkFragment;
import com.normanhoeller.beachesarefun.R;
import com.normanhoeller.beachesarefun.beaches.adapter.BeachAdapter;

import java.util.List;

/**
 * Created by norman on 22/04/17.
 */

public class BeachListFragment extends Fragment {

    private static final String TAG = BeachListFragment.class.getSimpleName();
    private BeachAdapter adapter;
    private RecyclerView recyclerView;


    public static BeachListFragment createInstance() {
        return new BeachListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_beaches_list, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.rv_beaches);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new BeachAdapter();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int currentPage = totalItemCount / NetworkFragment.PAGE_SIZE;
                if (lastVisibleItemPosition == totalItemCount - 1 && totalItemCount < 19) {
                    Log.d(TAG, "loading next page - current: " + currentPage);
//                    loadMoreItems(currentPage + 1);
                }
            }
        });
    }

    public void setBeaches(List<BeachModel> beaches) {
        Log.d(TAG, "got beaches: " + beaches.size());
        adapter.setBeachModelList(beaches);
    }

    private void loadMoreItems(int page) {
        NetworkFragment fragment = (NetworkFragment) getFragmentManager().findFragmentByTag(NetworkFragment.FRAG_TAG);
        if (fragment != null) {
            fragment.loadPageOfPictures(page);
        }
    }
}
