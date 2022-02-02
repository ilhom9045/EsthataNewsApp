package tj.esthata.newsapp.modules.splash.view

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith
import tj.esthata.newsapp.R

@RunWith(AndroidJUnit4ClassRunner::class)
class SplashFragmentTest {

    private var scene: FragmentScenario<SplashFragment> =
        launchFragmentInContainer(themeResId = R.style.Theme_EsthataNewsApp_Test)

    init {
        scene.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun `a`() {

    }

}