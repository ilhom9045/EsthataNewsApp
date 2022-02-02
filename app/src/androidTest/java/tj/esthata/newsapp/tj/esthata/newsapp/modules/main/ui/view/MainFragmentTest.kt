package tj.esthata.newsapp.tj.esthata.newsapp.modules.main.ui.view

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tj.esthata.newsapp.R
import tj.esthata.newsapp.modules.main.ui.view.MainFragment

@RunWith(AndroidJUnit4ClassRunner::class)
class MainFragmentTest {

    private lateinit var scenario: FragmentScenario<MainFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_EsthataNewsApp_Test)
        scenario.moveToState(newState = Lifecycle.State.STARTED)
    }

    @Test
    fun `test_select_home`() {
        onView(withId(R.id.navigation_home)).perform(click()).check(matches(isDisplayed()))
    }

    @Test
    fun `test_select_history`() {
        onView(withId(R.id.navigation_history)).perform(click()).check(matches(isDisplayed()))
    }

    @Test
    fun `test_select_favorite`() {
        onView(withId(R.id.navigation_favorite)).perform(click()).check(matches(isDisplayed()))
    }

    @Test
    fun `test_select_settings`() {
        onView(withId(R.id.navigation_settings)).perform(click()).check(matches(isDisplayed()))
    }
}