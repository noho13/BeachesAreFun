package com.normanhoeller.beachesarefun.beaches.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.normanhoeller.beachesarefun.BaseActivity;
import com.normanhoeller.beachesarefun.R;
import com.normanhoeller.beachesarefun.Utils;
import com.normanhoeller.beachesarefun.beaches.Beach;

import java.util.List;

public class BeachesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            BeachListFragment fragment = BeachListFragment.createInstance();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        }
        if (Utils.isNetworkAvailable(this)) {
            fragment.loadPageOfPictures(1);
        } else {
            showSnackBar(findViewById(android.R.id.content), getString(R.string.no_internet));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.beaches_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_me:
                return true;
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        if (fragment != null) {
            fragment.logoutCurrentUser();
        }
    }

    @Override
    public void setBeachesResult(List<Beach> beaches) {
        BeachListFragment beachListFragment = (BeachListFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
        if (beachListFragment != null) {
            beachListFragment.setBeaches(beaches);
        }
    }
}
