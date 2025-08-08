package toyproject.startofconversation.auth.controller.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import toyproject.startofconversation.auth.domain.policy.PasswordPolicy

data class LocalRegisterRequest(
    @field:Email(message = "이메일 형식이 올바르지 않습니다.")
    @field:NotBlank(message = "이메일은 필수 값입니다.")
    val email: String,

    @field:NotBlank
    @field:Pattern(
        regexp = PasswordPolicy.REGEX,
        message = PasswordPolicy.MESSAGE
    )
    val password: String,

    val nickname: String?
)