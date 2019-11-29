package app.service

import app.domain.CalendarUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserContextStub : UserContext {
    lateinit var user: CalendarUser

    override fun getCurrentUser(): CalendarUser = user

    @Autowired
    override fun setCurrentUser(user: CalendarUser) {
        this.user = user
    }
}