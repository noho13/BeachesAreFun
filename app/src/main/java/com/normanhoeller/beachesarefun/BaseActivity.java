package com.normanhoeller.beachesarefun;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.normanhoeller.beachesarefun.beaches.Beach;
import com.normanhoeller.beachesarefun.login.User;
import com.normanhoeller.beachesarefun.network.RetainedFragment;

import java.util.List;

/**
 * Created by norman on 22/04/17.
 */

public class BaseActivity extends AppCompatActivity implements Callback {

    protected RetainedFragment fragment;
    protected Snackbar snackbar;

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

    public RetainedFragment getRetainedFragment() {
        return fragment;
    }

    public void showSnackBar(View view, String text) {
        snackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void dismissSnackBar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissSnackBar();
        super.onDestroy();
    }

    @Override
    public void setBeachesResult(List<Beach> beaches) {
    }

    @Override
    public void setUserResult(User user) {
    }

    @Override
    public void handleError(BeachError error) {
        dismissSnackBar();
        String errorMessage = error != null ? error.getErrorMessage() : getString(R.string.error);
        showSnackBar(findViewById(android.R.id.content), errorMessage);
    }
}
