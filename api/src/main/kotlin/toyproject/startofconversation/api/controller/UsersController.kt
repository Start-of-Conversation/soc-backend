package toyproject.startofconversation.api.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.service.UsersService

@RestController
@RequestMapping("/api/users")
class UsersController(
    private val usersService: UsersService
) {
}