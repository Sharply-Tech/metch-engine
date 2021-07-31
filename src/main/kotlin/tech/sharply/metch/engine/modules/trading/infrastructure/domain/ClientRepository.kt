package tech.sharply.metch.engine.modules.trading.infrastructure.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.sharply.metch.engine.modules.trading.domain.model.Client

@Repository
interface ClientRepository : JpaRepository<Client, Long>