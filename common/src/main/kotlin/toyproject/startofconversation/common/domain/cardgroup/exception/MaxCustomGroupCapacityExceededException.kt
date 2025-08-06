package toyproject.startofconversation.common.domain.cardgroup.exception

import toyproject.startofconversation.common.exception.SOCDomainViolationException

class MaxCustomGroupCapacityExceededException(
    maxSize: Int, cause: Throwable? = null
) : SOCDomainViolationException("A user can create up to $maxSize custom groups only.", cause)