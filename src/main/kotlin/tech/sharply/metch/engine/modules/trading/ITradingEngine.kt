package tech.sharply.metch.engine.modules.trading

import tech.sharply.metch.engine.modules.trading.domain.model.Instrument
import tech.sharply.metch.orderbook.domain.model.orderbook.OrderBook

interface ITradingEngine {

    fun findAllInstruments(): Collection<Instrument>

    fun findInstrumentById(id: Long): Instrument?

    fun findOpenedInstruments(): Collection<Instrument>

    fun findOpenedInstrumentById(id: Long): Instrument?

    fun isOpened(instrument: Instrument): Boolean

    fun openInstrument(instrument: Instrument)

    fun findOrderBookByInstrument(instrument: Instrument): OrderBook?

}