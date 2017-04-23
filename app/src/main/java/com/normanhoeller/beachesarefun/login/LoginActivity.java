package com.normanhoeller.beachesarefun.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.normanhoeller.beachesarefun.BaseActivity;
import com.normanhoeller.beachesarefun.Utils;
import com.normanhoeller.beachesarefun.beaches.ui.BeachesActivity;

/**
 * Created by norman on 22/04/17.
 */

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(Utils.retrieveToken(this))) {
            Intent startBeaches = new Intent(this, BeachesActivity.class);
            startBeaches.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startBeaches);
        } else if (savedInstanceState == null) {
            LoginFragment fragment = LoginFragment.createInstance();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        }
    }
}
