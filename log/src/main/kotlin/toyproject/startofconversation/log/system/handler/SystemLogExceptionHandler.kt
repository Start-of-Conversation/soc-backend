package toyproject.startofconversation.log.system.handler

import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import toyproject.startofconversation.common.base.value.ModuleType
import toyproject.startofconversation.common.security.SecurityUtil
import toyproject.startofconversation.log.system.dto.SystemLogEvent

@RestControllerAdvice
class SystemLogExceptionHandler(
    private val eventPublisher: ApplicationEventPublisher
) {

    @ExceptionHandler(Exception::class)
    fun handleAll(e: Exception, request: HttpServletRequest) {
        val ip = request.remoteAddr
        val uri = request.requestURI

        eventPublisher.publishEvent(
            SystemLogEvent.error(
                userId = SecurityUtil.getCurrentUserIdOrNull(),
                ipAddress = ip,
                module = ModuleType.resolveFromURI(uri),
                action = uri,
                errorMessage = e.message
            )
        )
    }
}