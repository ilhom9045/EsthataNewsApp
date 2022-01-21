package tj.esthata.newsapp.repository.networkrepository.exception

import java.io.IOException

class InternetConnectionException(override val message: String?): IOException(message)