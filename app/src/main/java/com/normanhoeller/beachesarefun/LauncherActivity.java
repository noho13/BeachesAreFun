package com.normanhoeller.beachesarefun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.normanhoeller.beachesarefun.beaches.ui.BeachesActivity;
import com.normanhoeller.beachesarefun.login.LoginActivity;

/**
 * Created by normanMedicuja on 24/04/17.
 */

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent startNextActivity;
        if (!TextUtils.isEmpty(Utils.retrieveToken(this))) {
            startNextActivity = new Intent(this, BeachesActivity.class);
        } else {
            startNextActivity = new Intent(this, LoginActivity.class);
        }
        startActivity(startNextActivity);
        finish();
    }
}
