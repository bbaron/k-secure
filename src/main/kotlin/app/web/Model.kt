package app.web

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateEventForm(
        @NotBlank(message = "Attendee Email is required")
        @Email(message = "Attendee Email must be a valid email")
        var attendeeEmail: String? = null,
        @NotBlank(message = "Summary is required")
        var summary: String? = null,
        @NotBlank(message = "Description is required")
        var description: String? = null,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        @NotNull
        var scheduled: LocalDateTime? = null
)

data class SignupForm(
        @NotBlank(message = "First Name is required")
        var firstName: String? = null,
        @NotBlank(message = "Last Name is required")
        var lastName: String? = null,
        @Email(message = "Please provide a valid email address")
        @NotBlank(message = "Email is required")
        var email: String? = null,
        @NotBlank(message = "Password is required")
        var password: String? = null
)