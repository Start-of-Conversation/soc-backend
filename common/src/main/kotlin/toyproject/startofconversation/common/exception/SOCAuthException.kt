package toyproject.startofconversation.common.exception

/**
 * 인증 관련 Exception 모음
 */

class SOCUnauthorizedException(message: String, cause: Throwable? = null) : SOCException(message, cause)

class SOCForbiddenException(message: String, cause: Throwable? = null) : SOCException(message, cause)