package app.web

import app.domain.CalendarUser
import app.domain.Event
import app.service.CalendarService
import app.service.UserContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDateTime
import javax.validation.Valid

@Controller
@RequestMapping("/events")
class EventsController @Autowired constructor(private val calendarService: CalendarService, private val userContext: UserContext) {
    @GetMapping(path = ["", "/"])
    fun events(): ModelAndView {
        return ModelAndView("events/list", "events", calendarService.getEvents())
    }

    @GetMapping("/my")
    fun myEvents(): ModelAndView {
        val currentUser = userContext.getCurrentUser()
        val currentUserId = currentUser.id
        val result = ModelAndView("events/my", "events", calendarService.findForUser(currentUserId!!))
        result.addObject("currentUser", currentUser)
        return result
    }

    @GetMapping("/{eventId}")
    fun show(@PathVariable eventId: Int): ModelAndView {
        val event = calendarService.getEvent(eventId)
        return ModelAndView("events/show", "event", event)
    }

    @GetMapping("/form")
    fun createEventForm(model: Model): String {
        val createEventForm = CreateEventForm(
                summary = "A new event ...",
                description = "This was auto-populated to save time creating a valid event.",
                scheduled = LocalDateTime.now()
        )
        // make the attendee not the current user
        val currentUser: CalendarUser = userContext.getCurrentUser()
        val attendeeId = if (currentUser.id == 0) 1 else 0
        val attendee = calendarService.getUser(attendeeId)
        createEventForm.attendeeEmail = attendee.email
        model["createEventForm"] = createEventForm
        return "events/create"
    }

    /**
     * Populates the form for creating an event with valid information. Useful so that users do not have to think when
     * filling out the form for testing.
     *
     * @param createEventForm the form
     * @return the template path
     */
    @GetMapping(path = ["/new"], params = ["auto"])
    fun createEventFormAutoPopulate(@ModelAttribute createEventForm: CreateEventForm): String { // provide default values to make user submission easier
        createEventForm.summary = "A new event..."
        createEventForm.description = "This was auto populated to save time creating a valid event."
        createEventForm.scheduled = LocalDateTime.now()
        // make the attendee not the current user
        val currentUser: CalendarUser = userContext.getCurrentUser()
        val attendeeId = if (currentUser.id == 0) 1 else 0
        val attendee = calendarService.getUser(attendeeId)
        createEventForm.attendeeEmail = attendee.email
        return "events/create"
    }

    @PostMapping(path = ["/new"])
    fun createEvent(createEventForm: @Valid CreateEventForm, result: BindingResult,
                    redirectAttributes: RedirectAttributes): String {
        if (result.hasErrors()) {
            return "events/create"
        }
        val attendee = calendarService.findUserByEmail(createEventForm.attendeeEmail!!)
        if (attendee == null) {
            result.rejectValue("attendeeEmail", "attendeeEmail.missing",
                    "Could not find a user for the provided Attendee Email")
        }
        if (result.hasErrors()) {
            return "events/create"
        }
        val event = Event(attendee = attendee!!,
                description = createEventForm.description!!,
                owner = userContext.getCurrentUser(),
                summary = createEventForm.summary!!,
                scheduled = createEventForm.scheduled!!)
        calendarService.createEvent(event)
        redirectAttributes.addFlashAttribute("message", "Successfully added the new event")
        return "redirect:/events/my"
    }

}