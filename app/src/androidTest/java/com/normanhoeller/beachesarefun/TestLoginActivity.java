package com.normanhoeller.beachesarefun;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.normanhoeller.beachesarefun.login.LoginActivity;
import com.normanhoeller.beachesarefun.login.LoginFragment;
import com.normanhoeller.beachesarefun.network.RetainedFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by normanMedicuja on 26/04/17.
 */
@RunWith(AndroidJUnit4.class)
public class TestLoginActivity {

    private LoginActivity loginActivity;
    private LoginFragment loginFragment;
    private RetainedFragment retainedFragment;
    private static final String INVALID_EMAIL = "user1";
    private static final String INVALID_PASSWORD = "123";

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public void setup() {
        loginActivity = activityRule.getActivity();
        loginFragment = (LoginFragment) loginActivity.getSupportFragmentManager().findFragmentById(R.id.fl_container);
        retainedFragment = (RetainedFragment) loginActivity.getSupportFragmentManager().findFragmentByTag(RetainedFragment.FRAG_TAG);
    }

    @Test
    public void retainedFragment_notNull() {
        assertNotNull(retainedFragment);
    }

    @Test
    public void retainedFragment_shouldBeTheSame_afterConfigChange() {
        loginActivity.finish();
        LoginActivity activity = activityRule.getActivity();
        RetainedFragment retainedFragmentAfterConfigChange = (RetainedFragment) activity.getSupportFragmentManager().findFragmentByTag(RetainedFragment.FRAG_TAG);
        assertEquals(retainedFragment, retainedFragmentAfterConfigChange);
    }

    @Test
    public void loginFragment_notNull() {
        assertNotNull(loginFragment);
    }

    @Test
    public void login_without_credentials_snackBar_should_be_shown() {
        onView(withId(R.id.btn_login)).perform(click());
        onView(withText(loginActivity.getString(R.string.creds_illegal)))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));
    }


    @Test
    public void register_without_credentials_snackBar_should_be_shown() {
        onView(withId(R.id.btn_register)).perform(click());
        onView(withText(loginActivity.getString(R.string.creds_illegal)))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));
    }

    @Test
    public void login_with_invalid_email_snackBar_should_be_shown() {
        onView(withId(R.id.et_user_name)).perform(typeText(INVALID_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        onView(withText(loginActivity.getString(R.string.creds_illegal)))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));
    }

    @Test
    public void login_with_invalid_password_snackBar_should_be_shown() {
        onView(withId(R.id.et_user_name)).perform(typeText(INVALID_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        onView(withText(loginActivity.getString(R.string.creds_illegal)))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));
    }

    @Test
    public void toolbarTitle_shouldBe_Welcome() {
        onView(withText(loginActivity.getString(R.string.welcome)))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));
    }
}
