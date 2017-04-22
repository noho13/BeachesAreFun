package com.normanhoeller.beachesarefun;

import android.os.Bundle;

import com.normanhoeller.beachesarefun.beaches.BeachListFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            BeachListFragment fragment = BeachListFragment.createInstance();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        }
        fragment.loadPageOfPictures(1);
    }
}
