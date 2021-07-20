package tech.sharply.metch.engine

import tech.sharply.metch.orderbook.domain.model.orderbook.OrderBook
import tech.sharply.metch.orderbook.domain.model.orderbook.async.AsyncOrderBook
import tech.sharply.metch.orderbook.domain.model.orderbook.naive.ThreadSafeNaiveOrderBook

class TradingEngine {

    val orderBook: AsyncOrderBook = ThreadSafeNaiveOrderBook();

}