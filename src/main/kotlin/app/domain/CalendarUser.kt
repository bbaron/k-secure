package app.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
open class CalendarUser(
        @Id
        @GeneratedValue
        val id: Int? = null,
        @NotBlank
        val firstName: String,
        @NotBlank
        val lastName: String,
        @Column(unique = true)
        @Email
        val email: String,
        @NotBlank
        val passcode: String
) {
    constructor(user: CalendarUser) : this(id = user.id, firstName = user.firstName, lastName = user.lastName,
            email = user.email, passcode = user.passcode)

    override fun equals(other: Any?): Boolean =
            (other === this) || (other is CalendarUser && email == other.email)

    override fun hashCode(): Int {
        val (_, _, _, email, _) = this
        return email.hashCode()
    }

    override fun toString(): String {
        return "CalendarUser(id=$id, firstName='$firstName', lastName='$lastName', email='$email')"
    }

    operator fun component1(): Int? = id
    operator fun component2(): String = firstName
    operator fun component3(): String = lastName
    operator fun component4(): String = email
    operator fun component5(): String = passcode

}

interface CalendarUserDao : JpaRepository<CalendarUser, Int> {
    fun getById(id: Int): CalendarUser?

    fun findByEmail(email: String): CalendarUser?

    fun findCalendarUsersByEmailContaining(email: String): List<CalendarUser>

    @Query("from CalendarUser where email like 'admin%'")
    fun findAdminUsers(): List<CalendarUser>

    @Query("from CalendarUser where email not like 'admin%'")
    fun findNonAdminUsers(): List<CalendarUser>
}