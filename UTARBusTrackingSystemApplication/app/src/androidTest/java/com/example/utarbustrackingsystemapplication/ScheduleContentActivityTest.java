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
public class ScheduleContentActivityTest {
    @Rule
    public final ActivityTestRule<ScheduleContentActivity> rule = new ActivityTestRule<>(ScheduleContentActivity.class);

    @Test
    public void specificSchedule() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, NewsContentActivity.class);
        intent.putExtra("route", "Route 3 : Bandar Sg Long and Palm Walk 3");
        intent.putExtra("bus", "Bus 3 (BHG 7344)");
        intent.putExtra("topNote", "Bus Schedule 3 Mr. Hamid Bin Abdullah, 25 seater Mondays to Fridays only With Effective from 18 January 2016");
        intent.putExtra("bottomNote", "*Trip 7 Not Available for Fridays. # Trip 12 ONLY available on Fridays. The time of arrival may not be accurate due to traffic.");
        intent.putExtra("timetable", "No.|Depart from UTAR|Forest Green Condo|Green Acre Condo|Palm Walk|Garden Park|Time arriving UTAR/1|7.15AM|7.20AM|7.35AM|7.30AM|7.35AM|7.45AM/2|8.00AM|8.05AM|8.10AM|8.15AM|8.20AM|8.30AM/3|8.45AM|8.50AM|8.55AM|9.00AM|9.05AM|9.15AM/4|9.30AM|9.35AM|9.40AM|9.45AM|9.50AM|10.00AM/5|10.30AM|10.35AM|10.40AM|10.45AM|10.50AM|11.00AM/6|11.30AM|11.35AM|11.40AM|11.45AM|11.50AM|12.00PM/LUNCH|LUNCH|LUNCH|LUNCH|LUNCH|LUNCH|LUNCH/7*|1.30PM|1.35PM|1.40PM|1.45PM|1.50PM|2.00PM/8|2.30PM|2.35PM|2.40PM|2.45PM|2.50PM|3.00PM/9|4.30PM|4.35PM|4.40PM|4.45PM|4.50PM|5.00PM/10|5.50PM|5.55PM|6.00PM|6.05PM|6.10PM|6.20PM/11|6.50PM|6.55PM|7.00PM|7.05PM|7.10PM|7.20PM/12#|8.45PM|8.50PM|8.55PM|9.00PM|9.05PM|NIL/");
        intent.putExtra("date", "March 14, 2016");

        rule.launchActivity(intent);

        onView(withText("Route 3 : Bandar Sg Long and Palm Walk 3")).check(matches(isDisplayed()));
        onView(withText("Bus 3 (BHG 7344)")).check(matches(isDisplayed()));
    }
}
