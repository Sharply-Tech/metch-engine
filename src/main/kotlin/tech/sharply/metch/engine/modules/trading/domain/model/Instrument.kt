package tech.sharply.metch.engine.modules.trading.domain.model

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Instrument(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @Column(unique = true)
    val name: String,
    @Column(unique = true)
    val symbol: String
)