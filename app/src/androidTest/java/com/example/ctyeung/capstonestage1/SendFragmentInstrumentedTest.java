package com.example.ctyeung.capstonestage1;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class SendFragmentInstrumentedTest {

    @Rule
    public ActivityTestRule<TabActivity> mActivityRule = new ActivityTestRule<>(
            TabActivity.class);

    @Before
    public void init(){
        mActivityRule.getActivity()
                .getSupportFragmentManager()
                .beginTransaction();
    }

    @Test
    public void SubjectTextDisplayed()
    {
        onView(withId(R.id.txt_subject)).check(matches(withText("Subject")));
    }
}
