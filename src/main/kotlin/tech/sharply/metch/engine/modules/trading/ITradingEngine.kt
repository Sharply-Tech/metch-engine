package tech.sharply.metch.engine.modules.trading

import tech.sharply.metch.engine.modules.financial.domain.model.FinancialInfo
import tech.sharply.metch.engine.modules.trading.domain.model.Client
import tech.sharply.metch.engine.modules.trading.domain.model.Instrument
import tech.sharply.metch.orderbook.domain.model.orderbook.async.AsyncOrderBook

interface ITradingEngine {

    fun findOpenedInstruments(): Collection<Instrument>

    fun findOpenedInstrumentById(id: Long): Instrument?

    fun findOrderBookByInstrument(instrument: Instrument): AsyncOrderBook?


}