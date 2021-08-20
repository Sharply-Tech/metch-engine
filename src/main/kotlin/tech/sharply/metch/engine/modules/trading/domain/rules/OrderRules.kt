package tech.sharply.metch.engine.modules.trading.domain.rules

import tech.sharply.metch.engine.building_blocks.domain.rules.BusinessRule
import tech.sharply.metch.engine.building_blocks.domain.rules.BusinessRuleValidationException
import tech.sharply.metch.orderbook.domain.model.types.OrderType
import java.math.BigDecimal

data class OrderPriceIsValidRule(val orderType: OrderType, val price: BigDecimal?) : BusinessRule {

    override fun checkRule() {
        if (orderType != OrderType.MARKET && price == null) {
            throw BusinessRuleValidationException(
                this, "order.priceRequired",
                "The order price is required unless the order is a market order!"
            );
        }
    }

}

data class OrderSizeIsValidRule(val size: BigDecimal) : BusinessRule {

    override fun checkRule() {
        if (size <= BigDecimal.ZERO) {
            throw BusinessRuleValidationException(
                this, "order.invalidSize",
                "The order size must be > 0"
            )
        }
    }

}