package com.example.systemadminapp;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by LaiFany on 3/27/2016.
 */

@RunWith(AndroidJUnit4.class)
public class CompleteActivityTest {
    @Rule
    public final ActivityTestRule<CompleteActivity> rule = new ActivityTestRule<>(CompleteActivity.class);

    @Test
    public void startNewJourney() {
        onView(withId(R.id.setNewWaypoint)).perform(click());
        onView(withId(R.id.setNewWaypoint)).check(ViewAssertions.doesNotExist());
    }
}
