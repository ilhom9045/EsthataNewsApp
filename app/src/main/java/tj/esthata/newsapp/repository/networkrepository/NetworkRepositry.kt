package tj.esthata.newsapp.repository.networkrepository

import tj.esthata.newsapp.repository.networkrepository.api.Api

interface NetworkRepositry {
    fun getApi(): Api
}