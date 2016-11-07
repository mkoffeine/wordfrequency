package com.koffeine.wordfrequency2;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BasicBehaviour {

    @Rule
    public ActivityTestRule<WordFreqActivity> mActivityRule = new ActivityTestRule<>(WordFreqActivity.class);


    @Test
    public void test() {
        onView(withId(R.id.editTextInput)).perform(typeText("test"));
        onView(withId(R.id.editTextResult)).check(matches(withText(containsString("638"))));
        onView(withId(R.id.editTextResult)).check(matches(withText(containsString("testing"))));
        onView(withId(R.id.editTextInput)).perform(clearText());
        onView(withId(R.id.editTextInput)).perform(typeText("west"));
        onView(withId(R.id.editTextResult)).check(matches(withText(containsString("715"))));
        onView(withId(R.id.button_copy)).perform(click());
        onView(withId(R.id.button_clear)).perform(click());
        onView(withId(R.id.editTextResult)).check(matches(withText("")));
        onView(withId(R.id.button_past)).perform(click());
        onView(withId(R.id.editTextInput)).perform(typeText("ern"));
        onView(withId(R.id.editTextResult)).check(matches(withText(containsString("1709"))));
    }


}
