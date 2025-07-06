package toyproject.startofconversation.common.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import toyproject.startofconversation.common.base.dto.ExceptionResponse
import toyproject.startofconversation.common.base.value.Code
import toyproject.startofconversation.common.exception.external.FirebaseException
import toyproject.startofconversation.common.lock.exception.LockAcquisitionException

@RestControllerAdvice
class SOCExceptionHandler {

    @ExceptionHandler(LockAcquisitionException::class)
    fun handleLockAcquisitionException(ex: LockAcquisitionException): ResponseEntity<ExceptionResponse> =
        Code.LOCK_CONFLICT.toResponse(ex)

    @ExceptionHandler(SOCUnauthorizedException::class)
    fun handleSOCAuthException(ex: SOCUnauthorizedException): ResponseEntity<ExceptionResponse> =
        Code.UNAUTHORIZED.toResponse(ex)

    @ExceptionHandler(SOCDuplicateResourceException::class)
    fun handleDuplicateResourceException(ex: SOCDuplicateResourceException): ResponseEntity<ExceptionResponse> =
        Code.DUPLICATE_RESOURCE.toResponse(ex)

    @ExceptionHandler(SOCDomainViolationException::class)
    fun handleDomainViolationException(ex: SOCDomainViolationException): ResponseEntity<ExceptionResponse> =
        Code.DOMAIN_CONSTRAINT_VIOLATION.toResponse(ex)

    @ExceptionHandler(FirebaseException::class)
    fun handleFirebaseException(ex: FirebaseException): ResponseEntity<ExceptionResponse> =
        Code.FIREBASE_ERROR.toResponse(ex)

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