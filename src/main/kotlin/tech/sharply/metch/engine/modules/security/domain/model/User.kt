package tech.sharply.metch.engine.modules.security.domain.model

import javax.persistence.*

@Entity
@Table(schema = "SECURITY", name = "USER")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @Column(unique = true)
    val email: String,
    val password: String
)