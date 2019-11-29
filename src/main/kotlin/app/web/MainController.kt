package app.web

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {
    @GetMapping("/")
    fun root() = "redirect:/index"

    @GetMapping("/index")
    fun index() = "index"

    @GetMapping("/user/index")
    fun userIndex() = "user/index"

    @GetMapping("/login")
    fun login() = "login"

    @GetMapping("/login-error")
    fun loginError(model: Model): String {
        model["loginError"] = true
        return "login"
    }


}