package tech.sharply.metch.engine.modules.financial.domain

import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.DecimalMin

enum class AccountOperationType {
    DEPOSIT,
    WITHDRAWAL
}

enum class Currency {
    EUR,
    USD,
    RON
}

@Entity
@Table
class ClientAccountOperation(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @Enumerated(EnumType.STRING)
    val type: AccountOperationType,
    @DecimalMin("1")
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    val currencyCode: Currency
)
