package toyproject.startofconversation.common.domain.user.exception

import toyproject.startofconversation.common.exception.SOCForbiddenException

class DeletedUserException(
    id: String, cause: Throwable? = null
) : SOCForbiddenException("User $id has already been deleted", cause)