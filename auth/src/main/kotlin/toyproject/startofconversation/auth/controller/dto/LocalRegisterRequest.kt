package toyproject.startofconversation.auth.controller.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class LocalRegisterRequest(
    @field:Email
    val email: String,

    @field:NotBlank
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&#])[A-Za-z\\d@\$!%*?&#]{8,}$",
        message = "비밀번호 형식이 올바르지 않습니다. 8자 이상, 대소문자, 숫자, 특수문자 포함"
    )
    val password: String,

    val nickname: String?
)