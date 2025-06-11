package toyproject.startofconversation.common.domain.user.exception

import toyproject.startofconversation.common.exception.SOCDuplicateResourceException

class EmailAlreadyExistsException(
    email: String, cause: Throwable? = null
) : SOCDuplicateResourceException("Email $email is already exists", cause)