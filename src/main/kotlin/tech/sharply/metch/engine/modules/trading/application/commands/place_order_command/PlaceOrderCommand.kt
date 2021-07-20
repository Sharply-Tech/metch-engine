package tech.sharply.metch.engine.modules.trading.application.commands.place_order_command

import tech.sharply.metch.engine.modules.trading.domain.model.Instrument
import java.math.BigDecimal

data class PlaceOrderCommand(
    val instrument: Instrument,
    val price: BigDecimal,
    val qty: BigDecimal
)