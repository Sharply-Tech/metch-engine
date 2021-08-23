package tech.sharply.metch.engine.modules.trading

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tech.sharply.metch.engine.modules.trading.application.commands._internal.LoadAssetsCommand
import tech.sharply.metch.engine.modules.trading.domain.events.ClientOrderCancelledEvent
import tech.sharply.metch.engine.modules.trading.domain.events.ClientOrderPlacedEvent
import tech.sharply.metch.engine.modules.trading.domain.events.ClientOrderUpdatedEvent
import tech.sharply.metch.engine.modules.trading.domain.model.Instrument
import tech.sharply.metch.orderbook.domain.events.OrderCancelledEvent
import tech.sharply.metch.orderbook.domain.events.OrderPlacedEvent
import tech.sharply.metch.orderbook.domain.events.OrderUpdatedEvent
import tech.sharply.metch.orderbook.domain.events.TradeClosedEvent
import tech.sharply.metch.orderbook.domain.model.orderbook.OrderBook
import tech.sharply.metch.orderbook.domain.model.orderbook.OrderBookEventsHandler
import tech.sharply.metch.orderbook.domain.model.orderbook.naive.NaiveOrderBook
import tech.sharply.spring_disruptor_mediatr.mediator.Mediator
import javax.annotation.PostConstruct

@Component
class TradingEngine(
    @Autowired
    val mediator: Mediator
) : ITradingEngine {

    val allInstrumentsById: MutableMap<Long, Instrument> = HashMap()
    val openedInstrumentsById: MutableMap<Long, Instrument> = HashMap()
    val orderBooksByInstrumentId: MutableMap<Long, OrderBook> = HashMap()

    @PostConstruct
    private fun init() {
        loadAssets()
    }

    override fun isOpened(instrument: Instrument): Boolean {
        return orderBooksByInstrumentId.containsKey(instrument.id)
    }

    @Scheduled(fixedDelay = 5000)
    private fun loadAssets() {
        mediator.dispatchBlocking(LoadAssetsCommand())
    }

    override fun findAllInstruments(): Collection<Instrument> {
        return allInstrumentsById.values
    }

    override fun findInstrumentById(id: Long): Instrument? {
        return allInstrumentsById[id]
    }

    override fun findOpenedInstruments(): Collection<Instrument> {
        return openedInstrumentsById.values
    }

    override fun findOpenedInstrumentById(id: Long): Instrument? {
        return openedInstrumentsById[id]
    }

    override fun openInstrument(instrument: Instrument) {
        allInstrumentsById[instrument.id] = instrument
        openedInstrumentsById[instrument.id] = instrument
        orderBooksByInstrumentId[instrument.id] = NaiveOrderBook(object : OrderBookEventsHandler {
            override fun handle(event: OrderCancelledEvent) {
                mediator.publishEvent(ClientOrderCancelledEvent(this, event.order))
            }

            override fun handle(event: OrderPlacedEvent) {
                mediator.publishEvent(ClientOrderPlacedEvent(this, event.order))
            }

            override fun handle(event: OrderUpdatedEvent) {
                mediator.publishEvent(ClientOrderUpdatedEvent(this, event.order))
            }

            override fun handle(event: TradeClosedEvent) {
                TODO("Not yet implemented")
            }
        }, null)
    }

    override fun findOrderBookByInstrument(instrument: Instrument): OrderBook? {
        return orderBooksByInstrumentId[instrument.id]
    }

}