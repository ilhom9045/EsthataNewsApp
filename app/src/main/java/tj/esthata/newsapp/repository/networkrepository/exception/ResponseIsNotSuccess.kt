package tj.esthata.newsapp.repository.networkrepository.exception

import java.io.IOException

class ResponseIsNotSuccess(override val message: String?):IOException(message)