package toyproject.startofconversation.common.exception.external

import toyproject.startofconversation.common.exception.SOCServerException

class FirebaseException(
    message: String, cause: Throwable? = null
) : SOCServerException(message, cause)