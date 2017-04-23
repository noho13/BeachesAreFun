package com.normanhoeller.beachesarefun;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.normanhoeller.beachesarefun.network.NetworkFragment;

/**
 * Created by norman on 22/04/17.
 */

public class BaseActivity extends AppCompatActivity {

    protected NetworkFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWorker();
    }

    private void setupWorker() {
        fragment = (NetworkFragment) getSupportFragmentManager().findFragmentByTag(NetworkFragment.FRAG_TAG);
        if (fragment == null) {
            fragment = new NetworkFragment();
            getSupportFragmentManager().beginTransaction().add(fragment, NetworkFragment.FRAG_TAG).commit();
        }
    }
}
