package tech.sharply.metch.engine.modules.trading.application.commands.cancel_order_command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tech.sharply.metch.engine.building_blocks.application.commands.BusinessRuleValidator
import tech.sharply.metch.engine.building_blocks.domain.exceptions.ExceptionWithCode
import tech.sharply.metch.engine.modules.trading.ITradingEngine
import tech.sharply.metch.orderbook.domain.model.orderbook.Order
import tech.sharply.metch.orderbook.domain.model.orderbook.OrderBook
import tech.sharply.spring_disruptor_mediatr.mediator.Command
import tech.sharply.spring_disruptor_mediatr.mediator.CommandHandler

data class CancelOrderCommand(
    val orderId: Long
) : Command<Order>

@Component
class CancelOrderCommandHandler(
    @Autowired
    val tradingEngine: ITradingEngine
) : CommandHandler<CancelOrderCommand, Order>, BusinessRuleValidator {

    override fun handle(request: CancelOrderCommand): Order {
        var order: Order? = null
        var orderBook: OrderBook? = null
        for (instrument in tradingEngine.findOpenedInstruments()) {
            orderBook = tradingEngine.findOrderBookByInstrument(instrument) ?: continue
            order = orderBook.findById(request.orderId)
            if (order != null) {
                break
            }
        }

        if (order == null || orderBook == null) {
            throw ExceptionWithCode("order.orderNotFound", "No order found for id ${request.orderId}")
        }

        return orderBook.cancel(order.id)!!
    }

}