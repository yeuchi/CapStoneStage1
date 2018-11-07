package com.example.ctyeung.capstonestage1;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ctyeung.capstonestage1.data.ShapeHelper;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

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
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.ctyeung.capstonestage1", appContext.getPackageName());
    }

    @Test
    public void AllGridListItemsDisplayed()
    {
        onView(withId(R.id.pager)).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.rv_shapes)).check(matches(withListSize(15)));
    }

    private static Matcher<View> withListSize(final int count) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view)
            {
                RecyclerView recyclerView = view.findViewById(R.id.rv_shapes);
                int numChildren = recyclerView.getChildCount();
                return (count == numChildren) ? true : false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("");
            }
        };
    }

}
