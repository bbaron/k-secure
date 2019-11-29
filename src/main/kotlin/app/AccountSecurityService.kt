package app

import app.domain.AccountRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class AccountSecurityService(private val accountRepository: AccountRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val account = accountRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
        return User.withUsername(account.username)
                .password(account.password)
                .roles("USER")
                .build()
    }
}