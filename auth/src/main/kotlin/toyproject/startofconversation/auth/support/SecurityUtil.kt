package toyproject.startofconversation.auth.support

import org.springframework.security.core.context.SecurityContextHolder
import toyproject.startofconversation.common.exception.SOCUnauthorizedException

object SecurityUtil {

    fun getCurrentUserId(): String {
        val auth = SecurityContextHolder.getContext().authentication
        return auth?.principal as? String
            ?: throw SOCUnauthorizedException("No authenticated user")
    }

}