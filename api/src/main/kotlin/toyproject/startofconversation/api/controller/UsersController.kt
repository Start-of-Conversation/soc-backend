package toyproject.startofconversation.api.controller

import org.springframework.web.bind.annotation.*
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

//    @GetMapping("/mypage")
//    fun getUserInfo(@RequestHeader("Authorization") authorization: String):

}