package com.normanhoeller.beachesarefun.beaches.ui;

import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.normanhoeller.beachesarefun.BaseActivity;
import com.normanhoeller.beachesarefun.BeachError;
import com.normanhoeller.beachesarefun.R;
import com.normanhoeller.beachesarefun.Utils;
import com.normanhoeller.beachesarefun.beaches.Beach;
import com.normanhoeller.beachesarefun.login.User;
import com.normanhoeller.beachesarefun.network.BitmapStore;

import java.util.List;

public class BeachesActivity extends BaseActivity {

    private static final String TAG = BeachesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            BeachListFragment fragment = BeachListFragment.createInstance();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        }

        if (true /*Utils.isNetworkAvailable(this)*/) {
            // Flag to prevent loading for tests...alternatively one could Inject a Dependency that gets mocked in test
            if (getIntent().getBooleanExtra(Utils.START_LOADING, false)) {
                fragment.loadPageOfPictures(1);
            }
        } else {
            showSnackBar(findViewById(android.R.id.content), getString(R.string.no_connection));
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
                loadUserInfo();
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
            fragment.getContext().deleteDatabase(BitmapStore.DATABASE_NAME);
        }


    }

    private void loadUserInfo() {
        if (fragment != null) {
            fragment.loadUserInfo();
        }
    }

    @Override
    public void setBeachesResult(List<Beach> beaches) {
        flushCache();
        BeachListFragment beachListFragment = (BeachListFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
        if (beachListFragment != null) {
            beachListFragment.setBeaches(beaches);
        }
    }

    @Override
    public void setUserResult(User user) {
        showSnackBar(findViewById(android.R.id.content), String.format(getString(R.string.about_me_info),  user.getEmail()));
    }

    private void flushCache() {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }
}
