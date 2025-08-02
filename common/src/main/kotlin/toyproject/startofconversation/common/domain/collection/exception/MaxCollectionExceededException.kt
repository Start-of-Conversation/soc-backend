package toyproject.startofconversation.common.domain.collection.exception

import toyproject.startofconversation.common.exception.SOCDomainViolationException

class MaxCollectionExceededException(
    maxSize: Int, cause: Throwable? = null
) : SOCDomainViolationException("Collection can only have up to $maxSize.", cause)