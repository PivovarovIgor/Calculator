package ru.geekbrains.calculator.ui

import android.content.pm.ActivityInfo
import android.provider.Settings.Global.getString
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.android.material.internal.ContextUtils
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.geekbrains.calculator.R

class CalculatorActivityTest {

    private lateinit var scenario: ActivityScenario<CalculatorActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(CalculatorActivity::class.java)
    }

    @Test
    fun activity_is_resumed() {
        assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun type_1234567890_on_calculator(){
        onView(withId(R.id.key_1)).perform(click())
        onView(withId(R.id.key_2)).perform(click())
        onView(withId(R.id.key_3)).perform(click())
        onView(withId(R.id.key_4)).perform(click())
        onView(withId(R.id.key_5)).perform(click())
        onView(withId(R.id.key_6)).perform(click())
        onView(withId(R.id.key_7)).perform(click())
        onView(withId(R.id.key_8)).perform(click())
        onView(withId(R.id.key_9)).perform(click())
        onView(withId(R.id.key_0)).perform(click())
        onView(withId(R.id.scoreboard)).check(matches(withText("1234567890")))
        onView(isRoot()).perform(OrientationChangeAction.ORIENTATION_LANDSCAPE)
        onView(withId(R.id.scoreboard)).check(matches(withText("1234567890")))
    }

    @Test
    fun div_on_zero(){
        val ERROR_DIVISION_BY_ZERO_MESSAGE = "error division by zero"

        onView(withId(R.id.key_1)).perform(click())
        onView(withId(R.id.key_1)).perform(click())
        onView(withId(R.id.key_div)).perform(click())
        onView(withId(R.id.key_0)).perform(click())
        onView(withId(R.id.key_result)).perform(click())
        onView(withId(R.id.scoreboard)).check(matches(withText(ERROR_DIVISION_BY_ZERO_MESSAGE)))
        onView(withId(R.id.key_plus)).perform(click())
        onView(withId(R.id.key_4)).perform(click())
        onView(withId(R.id.key_plus)).perform(click())
        onView(withId(R.id.key_1)).perform(click())
        onView(withId(R.id.scoreboard)).check(matches(withText(ERROR_DIVISION_BY_ZERO_MESSAGE)))
        onView(withId(R.id.key_backspace)).perform(click())
        onView(withId(R.id.scoreboard)).check(matches(withText(ERROR_DIVISION_BY_ZERO_MESSAGE)))
        onView(withId(R.id.key_c)).perform(click())
        onView(withId(R.id.scoreboard)).check(matches(withText("0")))
    }

    @After
    fun tearDown() {
        scenario.close()
    }
}

class OrientationChangeAction(private val orientation: Int): ViewAction {

    companion object {
        val ORIENTATION_LANDSCAPE = OrientationChangeAction(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        val ORIENTATION_PORTRAIT = OrientationChangeAction(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    override fun getConstraints(): Matcher<View> = isRoot()

    override fun getDescription(): String = "change orientation to $orientation"

    override fun perform(uiController: UiController, view: View) {
        uiController.loopMainThreadUntilIdle()
        var activity = ContextUtils.getActivity(view.context) ?: let {
            if (view is ViewGroup) {
                for (i in 0..view.childCount) {
                    val activity = ContextUtils.getActivity(view.getChildAt(i).context)
                    if (activity != null) {
                        return@let activity
                    }
                }
            }
            null
        }
        activity?.run {
            requestedOrientation = orientation
        }
    }
}