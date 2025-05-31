package toyproject.startofconversation.common.domain.user.exception

import toyproject.startofconversation.common.exception.SOCNotFoundException

class UserNotFoundException(id: String) : SOCNotFoundException("User $id is not found.")