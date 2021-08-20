package tech.sharply.metch.engine.building_blocks.domain.exceptions

fun ExceptionWithCode(code: String, message: String): ExceptionWithCode {
    return ExceptionWithCode(code, message, arrayOf())
}

open class ExceptionWithCode(val code: String, message: String, val args: Array<Any>) : RuntimeException(message) {
}