package tj.esthata.newsapp.modules.main.home.ui.view

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tj.esthata.newsapp.R

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeFragmentTest {

    private lateinit var scene: FragmentScenario<HomeFragment>

    @Before
    fun setUp() {
        scene = launchFragmentInContainer(themeResId = R.style.Theme_EsthataNewsApp_Test)
        scene.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun `test_viewpager`() {
        onView(withId(R.id.home_viewpager)).perform(swipeLeft())
    }

    @Test
    fun `test_recyclerview_item_click`() {
        onView(withId(R.id.home))
    }

}