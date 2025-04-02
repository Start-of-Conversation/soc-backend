package toyproject.startofconversation.api.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.service.UserService
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController
@RequestMapping("/api/user")
class UserController(
    private val usersService: UserService
) {

    @DeleteMapping("/withdrawal")
    fun withdrawalUser(@RequestParam("id") userId: String): ResponseData<Boolean> =
        usersService.deleteUser(userId)
}