package tech.sharply.metch.engine.modules.security.domain.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import tech.sharply.metch.engine.modules.security.domain.model.UserPrincipal
import tech.sharply.metch.engine.modules.security.domain.model.UserRepository

@Service
class UserDetailsServiceImpl(
    @Autowired
    val userRepository: UserRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String?): UserDetails {
        val emailRegex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)"

        return try {
            if (email == null || !emailRegex.toRegex().matches(email)) {
                throw Exception("Invalid email $email!")
            }

            val user = userRepository.findByEmail(email)
            if (user.isEmpty) throw UsernameNotFoundException("No user found for username $email")

            // decode multi factor secret

            UserPrincipal(user.get().email, user.get().password, user.get().)
        } catch (ex: UsernameNotFoundException) {
            throw ex
        } catch (ex: Exception) {
            throw UsernameNotFoundException("Could not load user details for user: $email", ex)
        }
    }

}