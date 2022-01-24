package tj.esthata.newsapp.core.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tj.esthata.newsapp.modules.main.home.ui.vm.ViewPagerFragmentsViewModel
import tj.esthata.newsapp.modules.main.ui.vm.MainViewModel
import tj.esthata.newsapp.repository.networkrepository.NetworkRepositoryImpl
import tj.esthata.newsapp.repository.networkrepository.NetworkRepositry
import tj.esthata.newsapp.repository.sqlrepository.SqlRepository
import tj.esthata.newsapp.repository.sqlrepository.SqlRepositoryImpl

object AppModules {

    private val network = module {
        single<NetworkRepositry> { NetworkRepositoryImpl(androidContext()) }
    }

    private val sql = module {
        factory<SqlRepository> { SqlRepositoryImpl(get()) }
    }

    private val viewmodels = module {
        factory { MainViewModel() }
        factory { ViewPagerFragmentsViewModel() }
    }

    val modules = listOf(network, sql,viewmodels)
}