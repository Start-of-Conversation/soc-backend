package toyproject.startofconversation.api.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.dto.UserDataResponse
import toyproject.startofconversation.api.service.UserService
import toyproject.startofconversation.auth.util.SecurityUtil
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController
@RequestMapping("/api/user")
class UserController(
    private val usersService: UserService
) {

    @GetMapping("/mypage")
    fun getUserInfo(): ResponseData<UserDataResponse> = usersService.findUserById(SecurityUtil.getCurrentUserId())

    @DeleteMapping("/withdrawal")
    fun withdrawalUser(): ResponseData<Boolean> = usersService.deleteUser(SecurityUtil.getCurrentUserId())
}