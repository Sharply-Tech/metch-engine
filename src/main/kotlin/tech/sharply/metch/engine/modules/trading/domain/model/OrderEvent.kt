package tech.sharply.metch.engine.modules.trading.domain.model

import tech.sharply.metch.orderbook.domain.model.orderbook.Order
import tech.sharply.metch.orderbook.domain.model.types.OrderAction
import tech.sharply.metch.orderbook.domain.model.types.OrderType
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table
class OrderEvent(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    override val id: Long,
    @ManyToOne
    @JoinColumn(name = "CLIENT_ID")
    val client: Client,
    @ManyToOne
    @JoinColumn(name = "INSTRUMENT_ID")
    val instrument: Instrument,
    @Enumerated(EnumType.STRING)
    override val action: OrderAction,
    override val price: BigDecimal,
    override val size: BigDecimal,
    override val type: OrderType,
    override val createdAt: LocalDateTime,
    override val modifiedAt: LocalDateTime,
) : Order {

    override val clientId: Long
        get() = client.id

}
