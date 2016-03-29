package com.example.busdriverapp;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.format.DateUtils;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by LaiFany on 3/27/2016.
 */

@RunWith(AndroidJUnit4.class)
public class JourneyActivityTest{
    @Rule
    public final ActivityTestRule<JourneyActivity> rule = new ActivityTestRule<>(JourneyActivity.class);/*{
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, JourneyActivity.class);
            result.putExtra("route", "Route 1 : Bandar Sg Long and Palm Walk 1");
            result.putExtra("bus", "Bus 9 (BJU 7359)");
            result.putExtra("defaultRoute", "Route 1 : Bandar Sg Long and Palm Walk 1");
            result.putExtra("defaultBus", "Bus 9 (BJU 7359)");
            return result;
        }
    };*/
    //IntentServiceIdlingResource idlingResource;

    /*@Before
    public void before() {
        Instrumentation instrumentation
                = InstrumentationRegistry.getInstrumentation();
        Context ctx = instrumentation.getTargetContext();
        idlingResource = new IntentServiceIdlingResource(ctx);
        Espresso.registerIdlingResources(idlingResource);

    }

    @After
    public void after() {
        Espresso.unregisterIdlingResources(idlingResource);

    }*/

    /*@Before
    public void resetTimeout() {
        IdlingPolicies.setMasterPolicyTimeout(60, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(26, TimeUnit.SECONDS);
    }*/

    @Test
    public void completeJourney() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, JourneyActivity.class);
        intent.putExtra("route", "Route 1 : Bandar Sg Long and Palm Walk 1");
        intent.putExtra("bus", "Bus 9 (BJU 7359)");
        intent.putExtra("defaultRoute", "Route 1 : Bandar Sg Long and Palm Walk 1");
        intent.putExtra("defaultBus", "Bus 9 (BJU 7359)");

        rule.launchActivity(intent);

        onView(withText("Initializing GPS")).check(matches(isDisplayed()));

        // wait during 15 seconds for a view
        //onView(isRoot()).perform(waitId(R.id.complete, 1000));

        //onView(withId(R.id.complete)).perform(click());
        //onView(withId(R.id.complete)).check(ViewAssertions.doesNotExist());
    }

    /** Perform action of waiting for a specific view id. */
    /*public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }*/
}
