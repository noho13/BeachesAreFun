package com.normanhoeller.beachesarefun.beaches;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.normanhoeller.beachesarefun.beaches.network.MyAsyncTask;

import java.util.List;

/**
 * Created by norman on 22/04/17.
 */

public class NetworkFragment extends Fragment {

    public static final String FRAG_TAG = "network_fragment";
    private final static String TAG = NetworkFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void loadPageOfPictures(int page) {
        new MyAsyncTask(this).execute(String.valueOf(page));
    }

    public void setResult(List<BeachModel> beaches) {
        BeachListFragment beachListFragment = (BeachListFragment) getFragmentManager().findFragmentById(android.R.id.content);
        if (beachListFragment != null) {
            beachListFragment.setBeaches(beaches);
        }
    }
}
