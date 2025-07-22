package toyproject.startofconversation.common.domain.cardgroup.exception

import toyproject.startofconversation.common.exception.SOCDuplicateResourceException

class DuplicateLikeException(
    cardGroupId: String, userId: String, cause: Throwable? = null
) : SOCDuplicateResourceException("User [$userId] already liked cardGroup [$cardGroupId].", cause)