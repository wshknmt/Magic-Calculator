package com.example.magiccalculator;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    public void clickButton(int buttonId) {
        onView(withId(buttonId)).perform(click());
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.magiccalculator", appContext.getPackageName());
    }

    @Test
    public void testCalculations() {
            onView(withId(R.id.button4)).perform(click());
            onView(withId(R.id.button3)).perform(click());
            onView(withId(R.id.buttonMultiply)).perform(click());
            onView(withId(R.id.button2)).perform(click());
            onView(withId(R.id.buttonEquals)).perform(click());
            onView(withId(R.id.resultText)).check(ViewAssertions.matches(ViewMatchers.withText("86")));
    }

    @Test
    public void testDivision() {
            onView(withId(R.id.button3)).perform(click());
            onView(withId(R.id.button9)).perform(click());
            onView(withId(R.id.buttonDivide)).perform(click());
            onView(withId(R.id.button2)).perform(click());
            onView(withId(R.id.buttonEquals)).perform(click());
            onView(withId(R.id.resultText)).check(ViewAssertions.matches(ViewMatchers.withText("19.5")));
    }

    @Test
    public void testDivisionByZeroDoesNotCrash() {
        try {
            clickButton(R.id.button1);
            clickButton(R.id.button0);
            clickButton(R.id.buttonDivide);
            clickButton(R.id.button0);
            clickButton(R.id.buttonEquals);

        } catch (ArithmeticException e) {
            fail("Test failed due to ArithmeticException: " + e.getMessage());
        } catch (Exception e) {
            fail("Test failed due to unexpected exception: " + e.getMessage());
        }
    }
}