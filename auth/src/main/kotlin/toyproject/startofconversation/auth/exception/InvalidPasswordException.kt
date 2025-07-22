package toyproject.startofconversation.auth.exception

import toyproject.startofconversation.common.exception.SOCUnauthorizedException

class InvalidPasswordException(
    identifier: String, cause: Throwable? = null
) : SOCUnauthorizedException("Invalid password: $identifier", cause)