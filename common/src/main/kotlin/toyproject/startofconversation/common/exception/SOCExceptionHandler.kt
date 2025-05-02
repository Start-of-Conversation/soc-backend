package toyproject.startofconversation.common.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import toyproject.startofconversation.common.base.dto.ExceptionResponse
import toyproject.startofconversation.common.base.value.Code

@RestControllerAdvice
class SOCExceptionHandler {

    @ExceptionHandler(SOCUnauthorizedException::class)
    fun handleSOCAuthException(ex: SOCUnauthorizedException): ResponseEntity<ExceptionResponse> =
        Code.UNAUTHORIZED.toResponse(ex)

    @ExceptionHandler(SOCServerException::class)
    fun handleSOCServerException(ex: SOCServerException): ResponseEntity<ExceptionResponse> =
        Code.INTERNAL_ERROR.toResponse(ex)

    @ExceptionHandler(SOCForbiddenException::class)
    fun handleSOCServerException(ex: SOCForbiddenException): ResponseEntity<ExceptionResponse> =
        Code.FORBIDDEN.toResponse(ex)

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ExceptionResponse> =
        Code.BAD_REQUEST.toResponse(ex)

    private fun Code.toResponse(ex: Exception): ResponseEntity<ExceptionResponse> =
        ResponseEntity.status(this.httpStatus).body(ExceptionResponse(this, ex))

}