package tech.sharply.metch.engine.modules.security.infrastructure.domain.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.sharply.metch.engine.modules.security.domain.model.User
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

}