package tj.esthata.newsapp.repository.networkrepository

import tj.esthata.newsapp.repository.networkrepository.api.Api

interface NetworkRepository {
    fun getApi(): Api
}