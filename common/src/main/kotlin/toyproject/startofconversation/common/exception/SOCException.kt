package toyproject.startofconversation.common.exception

import java.lang.RuntimeException

open class SOCException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class SOCNotSavedException(message: String, cause: Throwable? = null) : SOCException(message, cause)

class SOCNotFoundException(message: String, cause: Throwable? = null) : SOCException(message, cause)