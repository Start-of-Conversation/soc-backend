package toyproject.startofconversation.common.base.value

import lombok.Getter
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import toyproject.startofconversation.common.exception.SOCException
import java.util.*

@Getter
@RequiredArgsConstructor
enum class Code(val code: Int, val httpStatus: HttpStatus, val message: String) {

    OK(200, HttpStatus.OK, "Ok"),

    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_ERROR(400, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "Requested resource is not found"),

    INTERNAL_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),

    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "User unauthorized");

    fun getMessage(e: Throwable): String = getMessage("$message - ${e.message}")

    fun getMessage(message: String?): String = message ?: this.message

    override fun toString(): String {
        return "$name ($code)"
    }

    companion object {

        fun valueOf(httpStatus: HttpStatus?): Code {
            if (httpStatus == null) {
                throw SOCException("HttpStatus is null.")
            }

            return Arrays.stream(entries.toTypedArray())
                .filter { errorCode -> errorCode.httpStatus === httpStatus }
                .findFirst()
                .orElseGet { getCodeByHttpStatus(httpStatus) }
        }

        private fun getCodeByHttpStatus(httpStatus: HttpStatus): Code {
            var code: Code? = null

            code = if (httpStatus.is4xxClientError) {
                BAD_REQUEST
            } else if (httpStatus.is5xxServerError) {
                INTERNAL_ERROR
            } else {
                OK
            }

            return code
        }
    }


}