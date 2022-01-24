package tj.esthata.newsapp.modules.splash.view

import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.airbnb.lottie.LottieAnimationView
import tj.esthata.newsapp.R
import tj.esthata.newsapp.core.fragment.BaseFragment
import tj.esthata.newsapp.modules.main.ui.view.MainFragment

class SplashFragment : BaseFragment(R.layout.splash_fragment) {

    private lateinit var lottieAnimationView: LottieAnimationView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        listener()
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    private fun listener() {
        lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.start_fragment_container, MainFragment())
                    .commit()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {
            }

        })
    }

    private fun init(view: View) {
        lottieAnimationView = view.findViewById(R.id.lottie_animation)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}