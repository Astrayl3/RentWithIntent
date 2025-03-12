package com.example.rentwithintent

import android.view.View
import android.widget.SeekBar
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

fun setProgress(progress: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return allOf(isAssignableFrom(SeekBar::class.java))
        }

        override fun getDescription(): String {
            return "Set a progress on a SeekBar"
        }

        override fun perform(uiController: UiController?, view: View?) {
            val seekBar = view as SeekBar
            seekBar.progress = progress
        }
    }
}

