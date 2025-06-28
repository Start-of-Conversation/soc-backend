package toyproject.startofconversation.common.base.dto

import toyproject.startofconversation.common.base.value.Code

open class ResponseInfo(val code: String, val message: String) {

    companion object {

        fun to(code: Code, message: String?): ResponseInfo = ResponseInfo(code.toString(), message.orEmpty())

    }
}
