package tech.sharply.metch.engine.modules.trading.application.commands.place_order_command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tech.sharply.metch.engine.building_blocks.application.commands.BusinessRuleValidator
import tech.sharply.metch.engine.building_blocks.domain.exceptions.ExceptionWithCode
import tech.sharply.metch.engine.modules.trading.ITradingEngine
import tech.sharply.metch.engine.modules.trading.domain.model.Client
import tech.sharply.metch.engine.modules.trading.domain.rules.CheckInstrumentIsOpened
import tech.sharply.metch.engine.modules.trading.domain.rules.OrderPriceIsValidRule
import tech.sharply.metch.engine.modules.trading.domain.rules.OrderSizeIsValidRule
import tech.sharply.metch.orderbook.domain.model.orderbook.Order
import tech.sharply.metch.orderbook.domain.model.types.OrderAction
import tech.sharply.metch.orderbook.domain.model.types.OrderType
import tech.sharply.spring_disruptor_mediatr.mediator.Command
import tech.sharply.spring_disruptor_mediatr.mediator.CommandHandler
import java.math.BigDecimal

data class PlaceOrderCommand(
    val instrumentId: Long,
    val client: Client,
    val price: BigDecimal?,
    val size: BigDecimal,
    val action: OrderAction,
    val type: OrderType
) : Command<Order>

@Component
class PlaceOrderCommandHandler(
    @Autowired
    val tradingEngine: ITradingEngine
) : CommandHandler<PlaceOrderCommand, Order>, BusinessRuleValidator {

    override fun handle(request: PlaceOrderCommand): Order {
        val orderBook = checkRule(CheckInstrumentIsOpened(request.instrumentId, tradingEngine))
        checkRule(OrderPriceIsValidRule(request.type, request.price))
        checkRule(OrderSizeIsValidRule(request.size))

        var price = request.price
        if (request.type == OrderType.MARKET) {
            // automatically determine a price that guarantees the order get's closed or throw exception
            val compatibleOrders = if (request.action == OrderAction.ASK) orderBook.findBestBids(50)
            else orderBook.findBestAsks(50)
            if (compatibleOrders.isEmpty()) {
                throw ExceptionWithCode(
                    "order.cannotDetermineMarketPrice.insufficientOrdersOnOppositeAction",
                    "Cannot determine market price because there aren't enough orders on the opposite action!"
                )
            }
            // TODO: Implement better algorithm for market price
            price = compatibleOrders.first().price
        }

        return orderBook.place(request.client.id, request.action, price!!, request.size, request.type)
    }

}