package toyproject.startofconversation.api.controller

import org.springframework.web.bind.annotation.*
import toyproject.startofconversation.api.dto.RegisterUserRequest
import toyproject.startofconversation.api.dto.UserDataResponse
import toyproject.startofconversation.api.dto.UserPasswordCheckRequest
import toyproject.startofconversation.api.service.UsersService
import toyproject.startofconversation.common.base.ResponseData

@RestController
@RequestMapping("/api/users")
class UsersController(
    private val usersService: UsersService
) {

    @PostMapping("/register/local")
    fun registerUser(@RequestBody request: RegisterUserRequest): ResponseData<UserDataResponse> =
        ResponseData.to(usersService.saveUserLocal(request))

    @GetMapping("/mypage")
    fun getUserInfo(
        @RequestHeader("Authorization") authorization: String,
        @RequestParam("id") id: String
    ): ResponseData<UserDataResponse> = ResponseData.to(usersService.getUserData(id))

    @PostMapping("/mypage")
    fun checkPassword(
        @RequestHeader("Authorization") authorization: String,
        @RequestBody request: UserPasswordCheckRequest
    ): ResponseData<Boolean> = usersService.checkPassword(request)
}