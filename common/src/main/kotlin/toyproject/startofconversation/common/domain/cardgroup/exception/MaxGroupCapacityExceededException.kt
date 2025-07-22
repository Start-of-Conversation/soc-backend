package toyproject.startofconversation.common.domain.cardgroup.exception

import toyproject.startofconversation.common.exception.SOCDomainViolationException

class MaxGroupCapacityExceededException(
    maxSize: Int, cause: Throwable? = null
) : SOCDomainViolationException("Group can only have up to $maxSize members.", cause)