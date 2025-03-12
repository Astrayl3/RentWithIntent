package com.example.rentwithintent

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun displayFirstItemOnLaunch() {
        onView(withId(R.id.item_name)).check(matches(withText("Drum")))
    }

    @Test
    fun nextButtonShowsNextItem() {
        onView(withId(R.id.next_button)).perform(click())
        onView(withId(R.id.item_name)).check(matches(withText("Guitar")))

        onView(withId(R.id.next_button)).perform(click())
        onView(withId(R.id.item_name)).check(matches(withText("Piano")))

        onView(withId(R.id.next_button)).perform(click())
        onView(withId(R.id.item_name)).check(matches(withText("Drum")))
    }

    @Test
    fun borrowButtonOpensDetailActivity() {
        onView(withId(R.id.borrow_button)).perform(click())
        onView(withId(R.id.detail_name)).check(matches(isDisplayed()))
    }
}
