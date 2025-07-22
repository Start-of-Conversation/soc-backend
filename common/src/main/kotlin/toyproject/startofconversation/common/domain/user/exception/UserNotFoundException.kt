package toyproject.startofconversation.common.domain.user.exception

import toyproject.startofconversation.common.exception.SOCNotFoundException

class UserNotFoundException(
    id: String, cause: Throwable? = null
) : SOCNotFoundException("User $id is not found.", cause)