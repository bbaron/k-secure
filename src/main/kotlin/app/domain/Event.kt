package app.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class Event(
        @Id
        @GeneratedValue
        val id: Int? = null,
        @NotBlank
        val summary: String,
        @NotBlank
        val description: String,
        val scheduled: LocalDateTime,
        @OneToOne(optional = false, fetch = FetchType.EAGER)
        val attendee: CalendarUser,
        @OneToOne(optional = false, fetch = FetchType.EAGER)
        val owner: CalendarUser
) {
    override fun equals(other: Any?): Boolean =
            (other === this) || (other is Event
                    && summary == other.summary
                    && description == other.description
                    && scheduled == other.scheduled
                    && owner == other.owner
                    && attendee == other.attendee)

    override fun hashCode(): Int {
        return Objects.hash(summary, description, scheduled, owner, attendee)
    }
}

interface EventDao : JpaRepository<Event, Int> {
    @Query(value = "from Event e where e.owner = :user or e.attendee = :user")
    fun findForUser(@Param("user") user: CalendarUser): List<Event>

    fun getById(id: Int): Event?

}