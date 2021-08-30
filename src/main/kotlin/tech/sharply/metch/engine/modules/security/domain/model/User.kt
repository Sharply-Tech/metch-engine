package tech.sharply.metch.engine.modules.security.domain.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.sharply.metch.engine.modules.security.infrastructure.converters.AttributeEncryptor
import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "SECURITY", name = "USER")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @Column(unique = true)
    val email: String,
    val password: String,
    @Convert(converter = AttributeEncryptor)
    val multiFactorSecret: String?
)

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

}