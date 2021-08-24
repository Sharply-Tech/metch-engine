package tech.sharply.metch.engine.modules.security.domain.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(schema = "SECURITY", name = "LOGIN_EVENT")
class LoginEvent(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @ManyToOne
    val user: User,
    val ip: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Repository
interface LoginEventRepository : JpaRepository<LoginEvent, Long>