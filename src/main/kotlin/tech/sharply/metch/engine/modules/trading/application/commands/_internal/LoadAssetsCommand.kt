package tech.sharply.metch.engine.modules.trading.application.commands._internal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tech.sharply.metch.engine.modules.trading.ITradingEngine
import tech.sharply.metch.engine.modules.trading.TradingEngine
import tech.sharply.metch.engine.modules.trading.infrastructure.domain.InstrumentRepository
import tech.sharply.spring_disruptor_mediatr.mediator.Command
import tech.sharply.spring_disruptor_mediatr.mediator.CommandHandler

class LoadAssetsCommand : Command<Unit>

@Component
class LoadAssetsCommandHandler(
    @Autowired
    val tradingEngine: ITradingEngine,
    @Autowired
    val instrumentRepository: InstrumentRepository
) : CommandHandler<LoadAssetsCommand, Unit> {

    override fun handle(request: LoadAssetsCommand) {
        val allInstruments = instrumentRepository.findAll()
        allInstruments.forEach {
            if (tradingEngine.findOpenedInstrumentById(it.id) != null) {
                return
            }

            tradingEngine.openInstrument(it)
        }
    }

}