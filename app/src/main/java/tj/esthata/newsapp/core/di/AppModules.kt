package tj.esthata.newsapp.core.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tj.esthata.newsapp.repository.nativerepository.NativeRepository
import tj.esthata.newsapp.repository.nativerepository.NativeRepositoryIml
import tj.esthata.newsapp.repository.networkrepository.NetworkRepositoryImpl
import tj.esthata.newsapp.repository.networkrepository.NetworkRepository
import tj.esthata.newsapp.repository.sqlrepository.SqlRepository
import tj.esthata.newsapp.repository.sqlrepository.SqlRepositoryImpl

object AppModules {

    private val network = module {
        single<NetworkRepository> { NetworkRepositoryImpl(androidContext(),get()) }
    }

    private val ndk = module {
        single<NativeRepository> { NativeRepositoryIml() }
    }

    private val sql = module {
        factory<SqlRepository> { SqlRepositoryImpl(get()) }
    }

    val modules = listOf(network, sql,ndk)
}