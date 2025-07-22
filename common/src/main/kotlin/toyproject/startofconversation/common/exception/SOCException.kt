package toyproject.startofconversation.common.exception

import java.lang.RuntimeException

open class SOCException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

open class SOCNotSavedException(message: String, cause: Throwable? = null) : SOCException(message, cause)

open class SOCNotFoundException(message: String, cause: Throwable? = null) : SOCException(message, cause)

open class SOCDomainViolationException(message: String, cause: Throwable? = null) : SOCException(message, cause)

open class SOCDuplicateResourceException(message: String, cause: Throwable? = null) : SOCDomainViolationException(message, cause)