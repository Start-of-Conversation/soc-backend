package toyproject.startofconversation.api.controller

import org.springframework.web.bind.annotation.*
import toyproject.startofconversation.api.service.UsersService
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController
@RequestMapping("/api/users")
class UsersController(
    private val usersService: UsersService
) {

    @DeleteMapping("/withdrawal")
    fun withdrawalUser(@RequestParam("id") userId: String): ResponseData<Boolean> =
        usersService.deleteUser(userId)
}