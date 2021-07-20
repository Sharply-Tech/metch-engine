package tech.sharply.metch.engine.modules.trading.infrastructure.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InstrumentRepository : JpaRepository<InstrumentRepository, Long>