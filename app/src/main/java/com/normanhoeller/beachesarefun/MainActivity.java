package com.normanhoeller.beachesarefun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.normanhoeller.beachesarefun.beaches.BeachListFragment;
import com.normanhoeller.beachesarefun.beaches.NetworkFragment;

public class MainActivity extends AppCompatActivity {

    private NetworkFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setupWorker();
        if (savedInstanceState == null) {
            BeachListFragment fragment = BeachListFragment.createInstance();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        }
        fragment.loadPageOfPictures(1);
    }

    private void setupWorker() {
        fragment = (NetworkFragment) getSupportFragmentManager().findFragmentByTag(NetworkFragment.FRAG_TAG);
        if (fragment == null) {
            fragment = new NetworkFragment();
            getSupportFragmentManager().beginTransaction().add(fragment, NetworkFragment.FRAG_TAG).commit();
        }
    }
}
