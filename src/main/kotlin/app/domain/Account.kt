package app.domain

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Account(
        @Id
        val id: Long,
        val username: String,
        val password: String
)

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByUsername(username: String?): Account?
}