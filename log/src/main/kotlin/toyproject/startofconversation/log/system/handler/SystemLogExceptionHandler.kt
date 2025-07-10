package toyproject.startofconversation.log.system.handler

import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import toyproject.startofconversation.common.base.value.LogLevel
import toyproject.startofconversation.common.base.value.ModuleType
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.security.SecurityUtil
import toyproject.startofconversation.log.system.domain.entity.SystemLog

@RestControllerAdvice
class SystemLogExceptionHandler(
    private val eventPublisher: ApplicationEventPublisher,
    private val userRepository: UsersRepository
) {

    @ExceptionHandler(Exception::class)
    fun handleAll(e: Exception, request: HttpServletRequest) {
        val ip = request.remoteAddr
        val uri = request.requestURI

        val user = SecurityUtil.getCurrentUserIdOrNull()?.let(userRepository::findByIdOrNull)

        eventPublisher.publishEvent(
            SystemLog(
                user = user,
                ipAddress = ip,
                module = ModuleType.resolveFromURI(uri),
                action = uri,
                logLevel = LogLevel.ERROR,
                errorMessage = e.message
            )
        )
    }
}