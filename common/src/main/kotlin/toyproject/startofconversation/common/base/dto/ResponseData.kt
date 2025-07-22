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

        fun <T> to(message: String?): ResponseData<T?> {
            return ResponseData(message.orEmpty(), null)
        }

        fun <T> to(data: T): ResponseData<T> {
            return ResponseData(data)
        }
    }


}