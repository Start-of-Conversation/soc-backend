package toyproject.startofconversation.common.security

import org.springframework.security.core.context.SecurityContextHolder
import toyproject.startofconversation.common.exception.SOCUnauthorizedException

object SecurityUtil {

    fun getCurrentUserId(): String =
        getCurrentUserIdOrNull() ?: throw SOCUnauthorizedException("No authenticated user")

    fun getCurrentUserIdOrNull(): String? =
        SecurityContextHolder.getContext().authentication?.principal as? String

}