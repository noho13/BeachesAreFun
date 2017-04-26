package com.normanhoeller.beachesarefun.login;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.normanhoeller.beachesarefun.BaseActivity;
import com.normanhoeller.beachesarefun.BeachError;
import com.normanhoeller.beachesarefun.R;

/**
 * Created by norman on 22/04/17.
 */

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            LoginFragment fragment = LoginFragment.createInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.fl_container, fragment).commit();
        }
    }

    @Override
    public void setErrorResult(BeachError error) {
        showSnackBar(findViewById(R.id.fl_container), error.getErrorText());
    }
}
