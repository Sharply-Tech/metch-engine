package tech.sharply.metch.engine.modules.trading

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tech.sharply.metch.engine.modules.trading.infrastructure.domain.InstrumentRepository
import tech.sharply.metch.orderbook.domain.model.orderbook.OrderBook
import tech.sharply.metch.orderbook.domain.model.orderbook.async.AsyncOrderBook
import tech.sharply.metch.orderbook.domain.model.orderbook.naive.ThreadSafeAsyncNaiveOrderBook

@Component
class TradingEngine(
    @Autowired
    val instrumentRepository: InstrumentRepository

) {

    val orderBook: AsyncOrderBook = ThreadSafeAsyncNaiveOrderBook()


}