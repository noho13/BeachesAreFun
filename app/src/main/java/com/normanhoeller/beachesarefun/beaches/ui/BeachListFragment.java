package com.normanhoeller.beachesarefun.beaches.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.normanhoeller.beachesarefun.BaseActivity;
import com.normanhoeller.beachesarefun.R;
import com.normanhoeller.beachesarefun.Utils;
import com.normanhoeller.beachesarefun.beaches.BeachModel;
import com.normanhoeller.beachesarefun.beaches.adapter.BeachAdapter;
import com.normanhoeller.beachesarefun.network.RetainedFragment;

import java.util.Arrays;
import java.util.List;

/**
 * Created by norman on 22/04/17.
 */

public class BeachListFragment extends Fragment {

    private static final String TAG = BeachListFragment.class.getSimpleName();
    private BeachAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private boolean loading;
    private boolean lastPageLoaded;


    public static BeachListFragment createInstance() {
        return new BeachListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_beaches_list, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.rv_beaches);
        progressBar = (ProgressBar) root.findViewById(R.id.pb_loading);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.tb_toolbar);
        BaseActivity activity = ((BaseActivity) getActivity());
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
            activity.setSupportActionBar(toolbar);
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Log.d(TAG, "spanWidth: " + metrics.widthPixels / 2);

        adapter = new BeachAdapter(activity.getRetainedFragment().getMemCache(), metrics.widthPixels / 2);
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = staggeredGridLayoutManager.getItemCount();
                int[] lastVisibleItemPosition = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);

                Arrays.sort(lastVisibleItemPosition);
                int currentPage = totalItemCount / Utils.PAGE_SIZE;
                if (!loading && lastVisibleItemPosition[1] == totalItemCount - 1 && !lastPageLoaded /*&& totalItemCount < 31*/) {
                    Log.d(TAG, "loading next page - current: " + currentPage);
                    loading = true;
                    loadMoreItems(currentPage + 1);
                }
            }
        });
    }

    public void setBeaches(List<BeachModel> beaches) {
        Log.d(TAG, "got beaches: " + beaches.size());
        loading = false;
        if (progressBar.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        if (beaches.size() > 0) {
            adapter.setBeachModelList(beaches);
        } else {
            lastPageLoaded = true;
        }
    }

    private void loadMoreItems(int page) {
        RetainedFragment fragment = (RetainedFragment) getFragmentManager().findFragmentByTag(RetainedFragment.FRAG_TAG);
        if (fragment != null && Utils.isNetworkAvailable(fragment.getContext())) {
            fragment.loadPageOfPictures(page);
        } else {
            ((BaseActivity) getActivity()).showSnackBar(getView(), getString(R.string.no_internet));
        }
    }
}
