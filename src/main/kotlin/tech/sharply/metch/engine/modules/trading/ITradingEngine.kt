package tech.sharply.metch.engine.modules.trading

import tech.sharply.metch.engine.modules.trading.domain.model.Instrument
import tech.sharply.metch.orderbook.domain.model.orderbook.OrderBook

interface ITradingEngine {

    fun findOpenedInstruments(): Collection<Instrument>

    fun findOpenedInstrumentById(id: Long): Instrument?

    fun openInstrument(instrument: Instrument)

    fun findOrderBookByInstrument(instrument: Instrument): OrderBook?

}