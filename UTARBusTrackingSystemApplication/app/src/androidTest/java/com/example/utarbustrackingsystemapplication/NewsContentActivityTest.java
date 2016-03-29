package com.example.utarbustrackingsystemapplication;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
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
public class NewsContentActivityTest {
    @Rule
    public final ActivityTestRule<NewsContentActivity> rule = new ActivityTestRule<>(NewsContentActivity.class);

    @Test
    public void specficNews() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, NewsContentActivity.class);
        intent.putExtra("newsId", "54");
        intent.putExtra("newsTitle", "For AndroidTest");
        intent.putExtra("newsDesc", "For AndroidTestt");
        intent.putExtra("newsContent", "For AndroidTesttt");
        intent.putExtra("date", "March 28, 2016");

        rule.launchActivity(intent);

        onView(withText("For AndroidTest")).check(matches(isDisplayed()));
        onView(withText("For AndroidTestt")).check(matches(isDisplayed()));
        onView(withText("For AndroidTesttt")).check(matches(isDisplayed()));
        onView(withText("March 28, 2016")).check(matches(isDisplayed()));
    }
}
