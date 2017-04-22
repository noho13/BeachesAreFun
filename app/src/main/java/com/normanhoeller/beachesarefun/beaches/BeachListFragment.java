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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void setBeaches(List<BeachModel> beaches) {
        Log.d(TAG, "got beaches: " + beaches.size());
        adapter.setBeachModelList(beaches);
    }
}
