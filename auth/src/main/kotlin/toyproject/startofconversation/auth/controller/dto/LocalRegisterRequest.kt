package toyproject.startofconversation.auth.controller.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import toyproject.startofconversation.auth.domain.policy.PasswordPolicy

data class LocalRegisterRequest(
    @field:Email
    val email: String,

    @field:NotBlank
    @field:Pattern(
        regexp = PasswordPolicy.REGEX,
        message = PasswordPolicy.MESSAGE
    )
    val password: String,

    val nickname: String?
)