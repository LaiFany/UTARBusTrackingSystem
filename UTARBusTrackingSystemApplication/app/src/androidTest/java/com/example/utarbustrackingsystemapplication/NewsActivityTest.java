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
public class NewsActivityTest {
    @Rule
    public final ActivityTestRule<NewsActivity> rule = new ActivityTestRule<>(NewsActivity.class);

    @Test
    public void news() {
        onView(withText("qweqw")).check(matches(isDisplayed()));
    }
}
