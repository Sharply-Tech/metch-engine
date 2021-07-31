package tech.sharply.metch.engine.modules.financial.domain.model

import tech.sharply.metch.engine.modules.trading.domain.model.Client
import java.math.BigDecimal

data class FinancialInfo(
    val client: Client,
    val ownedInstrumentsSizeById: Map<Long, BigDecimal>,
    val exposure: BigDecimal,
    val availableFunds: BigDecimal
)