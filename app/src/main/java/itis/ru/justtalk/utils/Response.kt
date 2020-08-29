package itis.ru.justtalk.utils

class Response<T>(val data: T?, val error: Throwable?) {

    companion object {
        fun <T> success(data: T): Response<T> = Response(data, null)

        fun <T> error(error: Throwable): Response<T> = Response(null, error)
    }
}
