package tech.sharply.metch.engine.modules.trading.domain.model

import javax.persistence.*

enum class Market {
    STOCKS,
    COMMODITIES,
    CRYPTO
}

@Entity
@Table
class Instrument(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @Column(unique = true)
    val name: String,
    @Column(unique = true)
    val symbol: String,
    @Enumerated(EnumType.STRING)
    val market: Market
)