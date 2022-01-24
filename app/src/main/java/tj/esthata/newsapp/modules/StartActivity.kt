package tj.esthata.newsapp.modules

import android.os.Bundle
import tj.esthata.newsapp.R
import tj.esthata.newsapp.core.activity.BaseActivity
import tj.esthata.newsapp.modules.splash.view.SplashFragment

class StartActivity:BaseActivity(R.layout.start_activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState==null){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.start_fragment_container,SplashFragment())
                .commit()
        }
    }
}