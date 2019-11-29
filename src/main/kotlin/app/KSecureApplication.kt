package app

import app.domain.CalendarUser
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class KSecureApplication {
    @Bean
    fun currentUser(): CalendarUser =
            CalendarUser(
                    id = 1,
                    firstName = "user",
                    lastName = "1",
                    email = "user1@example.com",
                    passcode = "user1")
}

fun main(args: Array<String>) {
    runApplication<KSecureApplication>(*args)
}
