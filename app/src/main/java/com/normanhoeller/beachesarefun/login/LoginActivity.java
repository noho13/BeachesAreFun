package com.normanhoeller.beachesarefun.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.normanhoeller.beachesarefun.BaseActivity;

/**
 * Created by norman on 22/04/17.
 */

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            LoginFragment fragment = LoginFragment.createInstance();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        }
    }
}
