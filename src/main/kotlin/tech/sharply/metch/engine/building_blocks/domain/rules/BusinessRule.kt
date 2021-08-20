package tech.sharply.metch.engine.building_blocks.domain.rules

import tech.sharply.metch.engine.building_blocks.domain.exceptions.ExceptionWithCode

@FunctionalInterface
interface BusinessRule {

    @Throws(BusinessRuleValidationException::class)
    fun checkRule()

}

fun BusinessRuleValidationException(rule: BusinessRule): BusinessRuleValidationException {
    return BusinessRuleValidationException(rule, "", "", arrayOf())
}

fun BusinessRuleValidationException(rule: BusinessRule, code: String): BusinessRuleValidationException {
    return BusinessRuleValidationException(rule, code, "", arrayOf())
}

fun BusinessRuleValidationException(
    rule: BusinessRule,
    code: String,
    message: String
): BusinessRuleValidationException {
    return BusinessRuleValidationException(rule, code, message, arrayOf())
}

class BusinessRuleValidationException(private val rule: BusinessRule, code: String, message: String, args: Array<Any>) :
    ExceptionWithCode(code, message, args) {

    override fun toString(): String {
        return "BusinessRuleValidationException(rule=$rule, code=$code, message=$message, args=$args)"
    }

}