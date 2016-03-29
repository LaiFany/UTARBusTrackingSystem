package com.example.utarbustrackingsystemapplication;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by LaiFany on 3/28/2016.
 */
@RunWith(AndroidJUnit4.class)
public class ScheduleActivityTest {
    @Rule
    public final ActivityTestRule<ScheduleActivity> rule = new ActivityTestRule<>(ScheduleActivity.class);

    @Test
    public void schedule() {
        onView(withText("Route 3 : Bandar Sg Long and Palm Walk 3")).check(matches(isDisplayed()));
    }
}
