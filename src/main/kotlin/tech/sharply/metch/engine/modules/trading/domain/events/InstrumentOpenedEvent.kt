package tech.sharply.metch.engine.modules.trading.domain.events

import org.springframework.context.ApplicationEvent
import tech.sharply.metch.engine.building_blocks.domain.events.GenericApplicationEvent
import tech.sharply.metch.engine.modules.trading.domain.model.Instrument

class InstrumentOpenedEvent(val instrument: Instrument, source: Any) : ApplicationEvent(source)
