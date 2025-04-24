package toyproject.startofconversation.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SOCExceptionHandler {

    @ExceptionHandler(SOCAuthException::class)
    fun handleSOCAuthException(ex: SOCAuthException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.message)

    @ExceptionHandler(SOCServerException::class)
    fun handleSOCServerException(ex: SOCServerException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<String> =
        ResponseEntity.badRequest().body(e.message)

}