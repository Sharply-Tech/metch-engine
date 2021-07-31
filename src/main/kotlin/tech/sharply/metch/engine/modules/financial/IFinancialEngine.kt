package tech.sharply.metch.engine.modules.financial

import tech.sharply.metch.engine.modules.financial.domain.model.FinancialInfo
import tech.sharply.metch.engine.modules.trading.domain.model.Client

interface IFinancialEngine {

    fun findClientFinancialInfo(client: Client): FinancialInfo

//    fun checkFundsAndRegisterOrderExposure(order: Order);

}