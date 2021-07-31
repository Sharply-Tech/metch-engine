package tech.sharply.metch.engine.modules.trading

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tech.sharply.metch.engine.modules.trading.domain.model.Instrument
import tech.sharply.metch.engine.modules.trading.infrastructure.domain.InstrumentRepository
import tech.sharply.metch.orderbook.domain.model.orderbook.async.AsyncOrderBook
import tech.sharply.metch.orderbook.domain.model.orderbook.naive.ThreadSafeAsyncNaiveOrderBook
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.PostConstruct

@Component
class TradingEngine(
    @Autowired
    val instrumentRepository: InstrumentRepository,
    @Autowired
    val eventPublisher: ApplicationEventPublisher
) : ITradingEngine{

    val instrumentsById: MutableMap<Long, Instrument> = ConcurrentHashMap()
    val orderBooksByInstrumentId: MutableMap<Long, AsyncOrderBook> = ConcurrentHashMap()

    @PostConstruct
    private fun init() {
        this.loadAssets()
    }

    private fun isOpened(instrument: Instrument): Boolean {
        return orderBooksByInstrumentId.containsKey(instrument.id)
    }

    private fun open(instrument: Instrument) {
        orderBooksByInstrumentId[instrument.id] = ThreadSafeAsyncNaiveOrderBook()
    }

    @Scheduled(fixedDelay = 5000)
    private fun loadAssets() {
        val allInstruments = instrumentRepository.findAll()
        allInstruments.forEach {
            if (isOpened(it)) {
                return
            }

        }
    }

    override fun findOpenedInstruments(): Collection<Instrument> {
        return instrumentsById.values
    }

    override fun findOpenedInstrumentById(id: Long): Instrument? {
        return instrumentsById[id]
    }

    override fun findOrderBookByInstrument(instrument: Instrument): AsyncOrderBook? {
        return orderBooksByInstrumentId[instrument.id]
    }

}