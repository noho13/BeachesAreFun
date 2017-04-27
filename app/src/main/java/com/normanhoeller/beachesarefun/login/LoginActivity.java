package com.normanhoeller.beachesarefun.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.normanhoeller.beachesarefun.BaseActivity;
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
    public void setUserResult(User user) {
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (fragment != null) {
            if (user != null && TextUtils.isEmpty(user.getErrorMessage())) {
                fragment.onSuccess();
            } else {
                dismissSnackBar();
                String errorMessage = user != null ? user.getErrorMessage() : getString(R.string.error);
                showSnackBar(fragment.getView(), errorMessage);
            }
        }
    }
}
