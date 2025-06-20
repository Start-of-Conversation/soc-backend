package toyproject.startofconversation.common.domain.user.exception

import toyproject.startofconversation.common.exception.SOCNotFoundException

class LikeNotFoundException(
    cardGroupId: String, userId: String, cause: Throwable? = null
) : SOCNotFoundException("Like not found for cardGroup [$cardGroupId] and user [$userId].", cause)