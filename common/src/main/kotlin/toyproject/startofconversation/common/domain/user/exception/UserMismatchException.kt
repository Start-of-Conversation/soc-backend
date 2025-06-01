package toyproject.startofconversation.common.domain.user.exception

import toyproject.startofconversation.common.exception.SOCForbiddenException

class UserMismatchException(
    id: String, cause: Throwable? = null
) : SOCForbiddenException("User $id does not match.", cause)