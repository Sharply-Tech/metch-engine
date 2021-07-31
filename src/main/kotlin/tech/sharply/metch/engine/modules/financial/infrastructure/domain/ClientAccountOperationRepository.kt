package tech.sharply.metch.engine.modules.financial.infrastructure.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.sharply.metch.engine.modules.financial.domain.ClientAccountOperation

@Repository
interface ClientAccountOperationRepository : JpaRepository<ClientAccountOperation, Long>