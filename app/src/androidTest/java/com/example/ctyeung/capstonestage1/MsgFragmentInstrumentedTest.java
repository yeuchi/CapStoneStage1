package com.example.ctyeung.capstonestage1;

import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

public class MsgFragmentInstrumentedTest {
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
    public void DefaultTextDisplayed()
    {
        onView(withId(R.id.pager)).perform(swipeLeft());
        String header = mActivityRule.getActivity().getBaseContext().getResources().getString(R.string.header_default_msg);
        onView(withId(R.id.txt_msg_header)).check(matches(withText(header)));

        String footer = mActivityRule.getActivity().getBaseContext().getResources().getString(R.string.footer_default_msg);
        onView(withId(R.id.txt_msg_footer)).check(matches(withText(footer)));
    }
}
