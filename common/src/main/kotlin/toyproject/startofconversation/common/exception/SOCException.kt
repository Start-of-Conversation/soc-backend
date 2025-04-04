package toyproject.startofconversation.common.exception

import java.lang.RuntimeException

open class SOCException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)