package toyproject.startofconversation.auth.exception

import toyproject.startofconversation.common.exception.SOCUnauthorizedException

class InvalidPasswordException(
    email: String, cause: Throwable? = null
) : SOCUnauthorizedException("Invalid password: $email", cause)