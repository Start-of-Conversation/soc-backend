package toyproject.startofconversation.auth.util

import org.springframework.security.core.context.SecurityContextHolder
import toyproject.startofconversation.common.exception.SOCAuthException

object SecurityUtil {

    fun getCurrentUserId(): String {
        val auth = SecurityContextHolder.getContext().authentication
        return auth?.principal as? String
            ?: throw SOCAuthException("No authenticated user")
    }

}