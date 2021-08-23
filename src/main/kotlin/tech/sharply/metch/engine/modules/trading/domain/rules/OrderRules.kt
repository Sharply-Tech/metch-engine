package tech.sharply.metch.engine.modules.trading.domain.rules

import tech.sharply.metch.engine.building_blocks.domain.exceptions.ExceptionWithCode
import tech.sharply.metch.engine.building_blocks.domain.rules.BusinessRule
import tech.sharply.metch.engine.building_blocks.domain.rules.BusinessRuleValidationException
import tech.sharply.metch.engine.modules.trading.ITradingEngine
import tech.sharply.metch.orderbook.domain.model.orderbook.OrderBook
import tech.sharply.metch.orderbook.domain.model.types.OrderType
import java.math.BigDecimal

data class OrderPriceIsValidRule(val orderType: OrderType, val price: BigDecimal?) : BusinessRule<Unit> {

    override fun checkRule() {
        if (orderType != OrderType.MARKET && price == null) {
            throw BusinessRuleValidationException(
                this, "order.priceRequired",
                "The order price is required unless the order is a market order!"
            );
        }
    }

}

data class OrderSizeIsValidRule(val size: BigDecimal) : BusinessRule<Unit> {

    override fun checkRule() {
        if (size <= BigDecimal.ZERO) {
            throw BusinessRuleValidationException(
                this, "order.invalidSize",
                "The order size must be > 0"
            )
        }
    }

}

data class CheckInstrumentIsOpened(val instrumentId: Long, val tradingEngine: ITradingEngine) : BusinessRule<OrderBook> {

    override fun checkRule(): OrderBook {
        val instrument = tradingEngine.findInstrumentById(instrumentId)
            ?: throw ExceptionWithCode("order.invalidInstrumentId",
                "The specified id is not a valid instrumentId")

        if (!tradingEngine.isOpened(instrument)) {
            throw ExceptionWithCode("order.instrumentNotOpened",
                "The instrument ${instrument.symbol} is not currently opened!")
        }

        return tradingEngine.findOrderBookByInstrument(instrument)
            ?: throw ExceptionWithCode(
                "order.orderBookNotOpenedForInstrument",
                "No order book is opened for instrument: $instrument",
                arrayOf(instrument)
            )
    }

}
