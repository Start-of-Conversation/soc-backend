package toyproject.startofconversation.common.lock.exception

import toyproject.startofconversation.common.exception.SOCException

class LockAcquisitionException(
    lockKey: String,
    timeoutMillis: Long,
    override val message: String = "Failed to acquire lock for key [$lockKey] within $timeoutMillis ms.",
    cause: Throwable? = null
) : SOCException(message, cause)