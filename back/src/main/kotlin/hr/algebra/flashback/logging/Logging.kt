package hr.algebra.flashback.logging

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class Logger(val logback: org.slf4j.Logger) {
    constructor(): this(LoggerFactory.getLogger("default.log"))
    constructor(name: String): this(LoggerFactory.getLogger(name))
    constructor(obj: Any): this(LoggerFactory.getLogger(obj::class.java))
    constructor(clazz: Class<*>): this(LoggerFactory.getLogger(clazz))

    companion object {
        var traceId: String? = null
    }

    private fun log(logger: (String, Throwable?) -> Unit, message: String, ex: Throwable? = null) {
        if (traceId != null) {
            logger("[$traceId] - $message", ex)
        } else {
            logger(message, ex)
        }
    }

    fun info(message: String, ex: Throwable? = null) {
        log(logback::info, message, ex)
    }
}