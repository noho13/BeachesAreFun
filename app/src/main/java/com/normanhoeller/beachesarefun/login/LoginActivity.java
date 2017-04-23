package com.normanhoeller.beachesarefun.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.normanhoeller.beachesarefun.BaseActivity;
import com.normanhoeller.beachesarefun.beaches.BeachesActivity;
import com.normanhoeller.beachesarefun.network.NetworkFragment;
import com.normanhoeller.beachesarefun.R;

/**
 * Created by norman on 22/04/17.
 */

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        String token = preferences.getString(NetworkFragment.TOKEN, null);
        if (!TextUtils.isEmpty(token)) {
            startActivity(new Intent(this, BeachesActivity.class));
            finish();
        } else if (savedInstanceState == null) {
            LoginFragment fragment = LoginFragment.createInstance();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        }
    }
}
