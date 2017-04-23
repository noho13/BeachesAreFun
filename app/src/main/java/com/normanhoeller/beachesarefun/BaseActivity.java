package com.normanhoeller.beachesarefun;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.normanhoeller.beachesarefun.network.RetainedFragment;

/**
 * Created by norman on 22/04/17.
 */

public class BaseActivity extends AppCompatActivity {

    protected RetainedFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWorker();
    }

    private void setupWorker() {
        fragment = (RetainedFragment) getSupportFragmentManager().findFragmentByTag(RetainedFragment.FRAG_TAG);
        if (fragment == null) {
            fragment = new RetainedFragment();
            getSupportFragmentManager().beginTransaction().add(fragment, RetainedFragment.FRAG_TAG).commit();
        }
    }
}
