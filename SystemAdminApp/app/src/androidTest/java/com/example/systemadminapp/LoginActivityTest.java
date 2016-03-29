package com.example.systemadminapp;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by LaiFany on 3/27/2016.
 */

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public final ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void login() {
        onView(withId(R.id.username)).perform(replaceText("admin"));
        onView(withId(R.id.password)).perform(replaceText("admin")).perform(closeSoftKeyboard());
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.login)).perform(click());

        onView(withId(R.id.login)).check(ViewAssertions.doesNotExist());
    }
}
