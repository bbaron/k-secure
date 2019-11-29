package app.service

import app.domain.CalendarUser

/**
 * Manages the current [CalendarUser]. This demonstrates how in larger applications it is good to abstract out
 * accessing the current user to return the application specific user rather than interacting with Spring Security
 * classes directly.
 *
 * @author Rob Winch
 */
interface UserContext {
    fun getCurrentUser() : CalendarUser

    fun setCurrentUser(user: CalendarUser)
}