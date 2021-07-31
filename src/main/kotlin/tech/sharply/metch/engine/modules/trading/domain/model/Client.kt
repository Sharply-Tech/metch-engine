package tech.sharply.metch.engine.modules.trading.domain.model

import javax.persistence.*

@Entity
@Table
class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    val name: String
)