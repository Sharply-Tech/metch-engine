package tech.sharply.metch.engine.building_blocks.application.commands

import tech.sharply.metch.engine.building_blocks.domain.rules.BusinessRule
import tech.sharply.metch.engine.building_blocks.domain.rules.BusinessRuleValidationException
import tech.sharply.spring_disruptor_mediatr.mediator.Command
import tech.sharply.spring_disruptor_mediatr.mediator.CommandHandler
import kotlin.jvm.Throws

interface BusinessRuleValidator {

    @Throws(BusinessRuleValidationException::class)
    fun checkRule(rule: BusinessRule) {
        rule.checkRule()
    }

}