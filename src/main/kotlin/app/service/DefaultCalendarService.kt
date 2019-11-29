package app.service

import app.domain.CalendarUser
import app.domain.CalendarUserDao
import app.domain.Event
import app.domain.EventDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class DefaultCalendarService(
        private val eventDao: EventDao,
        private val userDao: CalendarUserDao
) : CalendarService {
    override fun getEvent(eventId: Int): Event =
            eventDao.getById(eventId) ?: throw NotFoundException("Event with id $eventId not found")

    override fun createEvent(event: Event): Event {
        if (event.id != null) throw IllegalArgumentException("id ${event.id} must be null")
        return eventDao.save(event)
    }

    override fun findForUser(userId: Int): List<Event> =
            eventDao.findForUser(userDao.getOne(userId))

    override fun getEvents(): List<Event> =
            eventDao.findAll()

    override fun getUser(id: Int): CalendarUser =
            userDao.getById(id) ?: throw NotFoundException("user with $id not found")

    override fun findUserByEmail(email: String): CalendarUser? =
            userDao.findByEmail(email)

    override fun findUsersByEmail(partialEmail: String): List<CalendarUser> =
            userDao.findCalendarUsersByEmailContaining(partialEmail)

    override fun createUser(user: CalendarUser): CalendarUser {
        if (user.id != null) throw IllegalArgumentException("id ${user.id} must be null")
        return userDao.save(user)
    }
}