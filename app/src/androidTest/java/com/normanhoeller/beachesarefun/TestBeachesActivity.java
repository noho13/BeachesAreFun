package com.normanhoeller.beachesarefun;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.normanhoeller.beachesarefun.beaches.Beach;
import com.normanhoeller.beachesarefun.beaches.ui.BeachListFragment;
import com.normanhoeller.beachesarefun.beaches.ui.BeachesActivity;
import com.normanhoeller.beachesarefun.network.RetainedFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by normanMedicuja on 26/04/17.
 */
@RunWith(AndroidJUnit4.class)
public class TestBeachesActivity {

    private static final String ID = "someId";
    private static final String NAME = "someName";
    private static final String URL = "someURL";
    private static final String WIDTH = "233";
    private static final String HEIGTH = "400";
    private static final int EXPECTED_ITEMS = 1;
    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();
    @Rule
    public ActivityTestRule<BeachesActivity> activityRule = new ActivityTestRule<>(
            BeachesActivity.class);
    private BeachesActivity beachesActivity;
    private BeachListFragment beachListFragment;
    private RetainedFragment retainedFragment;

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

    @Test
    public void progressBar_shouldBeVisible() {
        onView(withId(R.id.pb_loading)).check(matches(isDisplayed()));
    }

    @Test
    public void progressBar_shouldBeHidden() throws Throwable {
        uiThreadTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                beachListFragment.setBeaches(new ArrayList<Beach>());
            }
        });

        onView(withId(R.id.pb_loading)).check(matches(not(isDisplayed())));
    }

    @Test
    public void adapter_shall_have_specific_amount_of_items() throws Throwable {
        final Beach item = new Beach(
                ID,
                NAME,
                null,
                WIDTH,
                HEIGTH);

        uiThreadTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                beachListFragment.setBeaches(Collections.singletonList(item));
            }
        });

        RecyclerView recyclerView = (RecyclerView) beachListFragment.getView().findViewById(R.id.rv_beaches);
        int actual = recyclerView.getAdapter().getItemCount();
        assertEquals(EXPECTED_ITEMS, actual);
    }

    @Test
    public void check_item_on_screen() throws Throwable {
        final Beach item = new Beach(
                ID,
                NAME,
                null,
                WIDTH,
                HEIGTH);

        uiThreadTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                beachListFragment.setBeaches(Collections.singletonList(item));
            }
        });

        onView(withText(NAME)).check(matches(isDisplayed()));
    }
}
