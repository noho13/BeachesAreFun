package com.normanhoeller.beachesarefun;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.normanhoeller.beachesarefun.beaches.ui.BeachListFragment;
import com.normanhoeller.beachesarefun.beaches.ui.BeachesActivity;
import com.normanhoeller.beachesarefun.login.LoginActivity;
import com.normanhoeller.beachesarefun.network.RetainedFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by normanMedicuja on 26/04/17.
 */
@RunWith(AndroidJUnit4.class)
public class TestBeachesActivity {

    private BeachesActivity beachesActivity;
    private BeachListFragment beachListFragment;
    private RetainedFragment retainedFragment;

    @Rule
    public ActivityTestRule<BeachesActivity> activityRule = new ActivityTestRule<>(
            BeachesActivity.class);

    @Before
    public void setup() {
        beachesActivity = activityRule.getActivity();
        beachListFragment = (BeachListFragment) beachesActivity.getSupportFragmentManager().findFragmentById(android.R.id.content);
        retainedFragment = (RetainedFragment) beachesActivity.getSupportFragmentManager().findFragmentByTag(RetainedFragment.FRAG_TAG);
    }

    @Test
    public void retainedFragment_notNull() {
        assertNotNull(retainedFragment);
    }

    @Test
    public void retainedFragment_shouldBeTheSame_afterConfigChange() {
        beachesActivity.finish();
        BeachesActivity activity = activityRule.getActivity();
        RetainedFragment retainedFragmentAfterConfigChange = (RetainedFragment) activity.getSupportFragmentManager().findFragmentByTag(RetainedFragment.FRAG_TAG);
        assertEquals(retainedFragment, retainedFragmentAfterConfigChange);
    }

    @Test
    public void loginFragment_notNull() {
        assertNotNull(beachListFragment);
    }



}
