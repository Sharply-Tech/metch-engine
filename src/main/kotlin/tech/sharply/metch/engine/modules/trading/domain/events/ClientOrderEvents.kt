package tech.sharply.metch.engine.modules.trading.domain.events

import tech.sharply.metch.orderbook.domain.model.orderbook.Order
import tech.sharply.metch.orderbook.domain.model.orderbook.Trade
import tech.sharply.spring_disruptor_mediatr.mediator.AppEvent


class ClientOrderPlacedEvent(source: Any, val order: Order) : AppEvent(source)

class ClientOrderCancelledEvent(source: Any, val order: Order) : AppEvent(source)

class ClientOrderUpdatedEvent(source: Any, val order: Order) : AppEvent(source)

class TransactionClosedEvent(source: Any, val trade: Trade) : AppEvent(source)