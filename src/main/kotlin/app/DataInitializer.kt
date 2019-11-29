package app

import app.domain.Account
import app.domain.AccountRepository
import app.domain.CalendarUser
import app.domain.Event
import app.service.CalendarService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class DataInitializer(val calendarService: CalendarService) {

    @Transactional
    @EventListener(ContextRefreshedEvent::class)
    fun run() {
        val users = listOf("user" to "1", "admin" to "1", "user" to "2").map {
            CalendarUser(email = "${it.first}${it.second}@example.com",
                    passcode = "${it.first}${it.second}",
                    firstName = it.first,
                    lastName = it.second)
        }.map {
            calendarService.createUser(it)
        }.map {
            println(it)
            it
        }
        listOf(
                Event(owner = users[0],
                        attendee = users[1],
                        summary = "Birthday Party",
                        description = "This is going to be a great birthday",
                        scheduled = LocalDateTime.of(2013, 10, 4, 20, 30)),
                Event(owner = users[2],
                        attendee = users[0],
                        summary = "Conference Call",
                        description = "Call with the client",
                        scheduled = LocalDateTime.of(2013, 12, 23, 13, 0)),
                Event(owner = users[1],
                        attendee = users[2],
                        summary = "Lunch",
                        description = "Eating lunch together",
                        scheduled = LocalDateTime.of(2014, 1, 23, 11, 30))
        ).map {
            calendarService.createEvent(it)
        }.forEach {
            println(it)
        }
        val events = calendarService.findForUser(users[0].id!!)
        println(events)
    }
}