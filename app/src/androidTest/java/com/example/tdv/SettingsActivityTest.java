package com.example.tdv;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class SettingsActivityTest {
    @Rule
    public ActivityTestRule<SettingsActivity> mActivity = new ActivityTestRule<SettingsActivity>(SettingsActivity.class, false, true);


    @Test
    public void onClick() {
        onView(withId(R.id.textStep))
                .perform(typeText("0"));

        onView(withId(R.id.textTime))
                .perform(typeText("2000"));

        onView(withId(R.id.btnRead))
                .check(matches(not(isEnabled())));
    }
}