package com.example.ctyeung.capstonestage1;

import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ThirdFragmentInstrumentedTest {
    @Rule
    public ActivityTestRule<TabActivity> mActivityRule = new ActivityTestRule<>(
            TabActivity.class);

    @Before
    public void init() {
        mActivityRule.getActivity()
                .getSupportFragmentManager()
                .beginTransaction();
    }

    @Test
    public void AllGridListItemsDisplayed() {
        onView(withId(R.id.pager)).perform(swipeLeft()).perform(swipeLeft());

        String msg = mActivityRule.getActivity().getBaseContext().getResources().getString(R.string.shape_selection);
        onView(withId(R.id.textView)).check(matches(withText(msg)));

        onView(withId(R.id.rv_shapes)).check(matches(withListSize(15)));

    }

    private static Matcher<View> withListSize(final int count) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view)
            {
                RecyclerView recyclerView = view.findViewById(R.id.rv_shapes);
                int numChildren = recyclerView.getChildCount();
                //assertEquals(numChildren, count);

                return (count == numChildren) ? true : false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("");
            }
        };
    }
}
