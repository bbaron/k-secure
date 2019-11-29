package app.service

import app.domain.CalendarUser
import app.domain.Event

interface CalendarService {
    /**
     * Gets a [CalendarUser] for a specific [CalendarUser.id].
     *
     * @param id the [CalendarUser.id] of the [CalendarUser] to find.
     * @return a [CalendarUser] for the given id. Cannot be null.
     * @throws NotFoundException if the [CalendarUser] cannot be found
     */
    fun getUser(id: Int): CalendarUser

    /**
     * Finds a given [CalendarUser] by email address.
     *
     * @param email the email address to use to find a [CalendarUser].
     * @return a [CalendarUser] for the given email or null if one could not be found.
     */
    fun findUserByEmail(email: String): CalendarUser?

    /**
     * Finds any [CalendarUser] that has an email that starts with `partialEmail`.
     *
     * @param partialEmail the email address to use to find [CalendarUser]s. Cannot be null or empty String.
     * @return a List of [CalendarUser]s that have an email that starts with given partialEmail. If no results are found an empty List will be returned.
     */
    fun findUsersByEmail(partialEmail: String): List<CalendarUser>

    /**
     * Creates a new [CalendarUser].
     *
     * @param user the new [CalendarUser] to create. The [CalendarUser.id] must be null.
     * @return the new [CalendarUser].
     * @throws IllegalArgumentException if [CalendarUser.id] is non-null.
     */
    fun createUser(user: CalendarUser): CalendarUser

    /**
     * Given an id gets an [Event].
     *
     * @param eventId the [Event.id]
     * @return the [Event].
     * @throws NotFoundException if the [Event] cannot be found.
     */
    fun getEvent(eventId: Int): Event

    /**
     * Creates a [Event] and returns the new id for that [Event].
     *
     * @param event the [Event] to create. Note that the [Event.id] should be null.
     * @return the new id for the [Event]
     * @throws IllegalArgumentException if [Event.id] is non-null.
     */
    fun createEvent(event: Event): Event

    /**
     * Finds the [Event]'s that are intended for the [CalendarUser].
     *
     * @param userId the [CalendarUser.id] to obtain [Event]'s for.
     * @return [List] of [Event]'s intended for the specified [CalendarUser]. If the
     * [CalendarUser] does not exist an empty List will be returned.
     */
    fun findForUser(userId: Int): List<Event>

    /**
     * Gets all the available [Event]'s.
     *
     * @return [List] of [Event]'s
     */
    fun getEvents(): List<Event>
}
