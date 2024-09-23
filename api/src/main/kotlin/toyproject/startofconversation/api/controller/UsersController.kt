package toyproject.startofconversation.api.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.dto.RegisterUserRequest
import toyproject.startofconversation.api.service.UsersService

@RestController
@RequestMapping("/api/users")
class UsersController(
    private val usersService: UsersService
) {

    @PostMapping("/register/local")
    fun registerUser(@RequestBody request: RegisterUserRequest) =
        usersService.saveUserLocal(request)

}