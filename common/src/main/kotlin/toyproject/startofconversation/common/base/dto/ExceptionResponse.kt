package toyproject.startofconversation.common.base.dto

import toyproject.startofconversation.common.base.value.Code

class ExceptionResponse(code: Code, ex: Exception?) :
    ResponseInfo(code.toString(), ex?.message ?: code.message)