package com.normanhoeller.beachesarefun;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.normanhoeller.beachesarefun.beaches.BeachListFragment;
import com.normanhoeller.beachesarefun.beaches.BeachModel;
import com.normanhoeller.beachesarefun.beaches.network.ListAsyncTask;
import com.normanhoeller.beachesarefun.login.LoginAsyncTask;

import java.util.List;

/**
 * Created by norman on 22/04/17.
 */

public class NetworkFragment extends Fragment {

    public static final String FRAG_TAG = "network_fragment";
    private final static String TAG = NetworkFragment.class.getSimpleName();
    public static final int PAGE_SIZE = 6;

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
        new ListAsyncTask(this).execute(String.valueOf(page));
    }

    public void setResult(List<BeachModel> beaches) {
        BeachListFragment beachListFragment = (BeachListFragment) getFragmentManager().findFragmentById(android.R.id.content);
        if (beachListFragment != null) {
            beachListFragment.setBeaches(beaches);
        }
    }

    public void postPayload(String url, String payload) {
        new LoginAsyncTask().execute(url, payload);
    }

    public void loginUser(String userName, String password) {

    }

    public void logoutCurrentUser() {

    }
}
