package tech.sharply.metch.engine.building_blocks.infrastructure.dto

import tech.sharply.metch.engine.building_blocks.domain.exceptions.ExceptionWithCode

class ExceptionDTO(val code: String, val message: String?, val args: Array<Any>?) {

    constructor(ex: ExceptionWithCode) : this(
        ex.code,
        ex.message,
        ex.args
    )

}