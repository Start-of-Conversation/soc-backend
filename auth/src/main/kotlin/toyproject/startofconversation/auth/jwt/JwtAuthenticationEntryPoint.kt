package toyproject.startofconversation.auth.jwt

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver
import toyproject.startofconversation.common.logger.logger

@Component
class JwtAuthenticationEntryPoint(
    @Qualifier("handlerExceptionResolver") private val resolver: HandlerExceptionResolver
) : AuthenticationEntryPoint {
    private val log = logger()

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        if (request != null && response != null && authException != null) {
            request.setAttribute("exception", authException)
            resolver.resolveException(request, response, null, authException)
        } else {
            log.info("Request, Response, or AuthException is null")
        }
    }
}