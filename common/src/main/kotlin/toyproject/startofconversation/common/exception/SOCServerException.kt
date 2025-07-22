package toyproject.startofconversation.common.exception

open class SOCServerException(message: String, cause: Throwable? = null) : SOCException(message, cause)

class SOCUploadException(message: String, cause: Throwable? = null) : SOCServerException(message, cause)