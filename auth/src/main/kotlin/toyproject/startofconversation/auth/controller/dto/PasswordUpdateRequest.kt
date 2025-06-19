package toyproject.startofconversation.auth.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import toyproject.startofconversation.auth.domain.policy.PasswordPolicy

data class PasswordUpdateRequest(
    val oldPassword: String,

    @field:NotBlank
    @field:Pattern(
        regexp = PasswordPolicy.REGEX,
        message = PasswordPolicy.MESSAGE
    )
    val newPassword: String
)