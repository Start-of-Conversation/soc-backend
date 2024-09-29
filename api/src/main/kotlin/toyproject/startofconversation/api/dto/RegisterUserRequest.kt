package toyproject.startofconversation.api.dto

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import toyproject.startofconversation.common.domain.user.Users
import toyproject.startofconversation.common.domain.user.value.LoginChannel
import toyproject.startofconversation.common.domain.user.value.Role
import java.time.LocalDateTime

data class RegisterUserRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val profileImage: String?,
    val role: Role = Role.USER,
    val loginChannel: LoginChannel = LoginChannel.LOCAL,
    val acceptMarketing: Boolean = false,
    val acceptMarketingDate: LocalDateTime? = null
) {

    fun toUser(): Users {
        return Users(
            email = email,
            password = BCryptPasswordEncoder().encode(password),
            nickname = nickname,
            profileImage = profileImage,
            role = role,
            loginChannel = loginChannel,
            acceptMarketing = acceptMarketing,
            acceptMarketingDate = acceptMarketingDate
        )
    }

}
