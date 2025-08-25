package toyproject.startofconversation.common.base.dto

import toyproject.startofconversation.common.base.value.Code

open class ResponseData<T> : ResponseInfo {

    val data: T?

    constructor(data: T) : super(Code.OK.toString(), Code.OK.message) {
        this.data = data
    }

    constructor(message: String, data: T) : super(Code.OK.toString(), message) {
        this.data = data
    }

    companion object {

        fun <T> to(message: String?, wrapper: T): ResponseData<T> {
            return ResponseData(message.orEmpty(), wrapper)
        }

        fun <T> to(data: T): ResponseData<T> {
            return ResponseData(data)
        }
    }

}

inline fun <T> responseOf(message: String?, data: T) = ResponseData.to(message, data)
inline fun <T> responseOf(data: T) = ResponseData.to(data)
inline fun <T> responseMessageOf(message: String?) = ResponseData.to(message, null)