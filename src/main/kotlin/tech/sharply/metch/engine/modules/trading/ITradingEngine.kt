package tech.sharply.metch.engine.modules.trading

import tech.sharply.metch.engine.modules.trading.domain.model.Instrument
import tech.sharply.metch.orderbook.domain.model.orderbook.async.AsyncOrderBook

interface ITradingEngine {

    fun findOrderBookByInstrument(instrument: Instrument): AsyncOrderBook

}