package tj.esthata.newsapp.modules.main.home.ui.view

import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith
import tj.esthata.newsapp.R
import tj.esthata.newsapp.modules.main.home.ui.adapter.NewsRecyclerViewAdapter

@RunWith(AndroidJUnit4ClassRunner::class)
class ViewPagerFragmentTest {

    private var scene: FragmentScenario<ViewPagerFragment> =
        launchFragmentInContainer(themeResId = R.style.Theme_EsthataNewsApp_Test)

    init {
        scene.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun `recyclerview_item_click`() {
        onView(withId(R.id.history_recyclerview))
            .perform(
                clickRecyclerViewItem(0, R.id.newsContainer)
            )
    }

    private fun clickRecyclerViewItem(position: Int, id: Int? = null): ViewAction {
        return actionOnItemAtPosition<NewsRecyclerViewAdapter.ViewHolder>(position,
            id?.let {
                object : ViewAction {
                    override fun getConstraints() = null
                    override fun getDescription() = "Click on an item view with specified id."

                    override fun perform(uiController: UiController?, view: View?) {
                        val itemView = view?.findViewById<View>(id)
                            ?: throw Exception("Item view is not found")
                        itemView.performClick()
                    }
                }
            } ?: click())
    }

}