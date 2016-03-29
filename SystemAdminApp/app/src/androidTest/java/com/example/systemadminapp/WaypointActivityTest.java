package com.example.systemadminapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by LaiFany on 3/28/2016.
 */
@RunWith(AndroidJUnit4.class)
public class WaypointActivityTest {
    @Rule
    public final ActivityTestRule<WaypointActivity> rule = new ActivityTestRule<>(WaypointActivity.class);

    @Test
    public void setWaypoint() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, WaypointActivity.class);
        intent.putExtra("routeNo", "1");
        intent.putExtra("routeName", "Bandar Sg Long and Palm Walk 1");
        intent.putExtra("routeId", "1");
        intent.putExtra("bus", "Bus 9 (BJU 7359)");
        intent.putExtra("coordinates", "3.06891,101.78851|3.077992,101.710956|3.0541022627819188,101.79648440894088|3.0385787368682413,101.79395183199017");
        intent.putExtra("stopNames", "Primo|TBS|home|dhdh");

        rule.launchActivity(intent);

        onView(withText("Initializing GPS")).check(matches(isDisplayed()));
    }
}
