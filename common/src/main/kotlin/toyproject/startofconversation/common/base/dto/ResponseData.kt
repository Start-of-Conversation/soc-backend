package toyproject.startofconversation.common.base.dto

import toyproject.startofconversation.common.base.value.Code

open class ResponseData<T> : ResponseInfo {

    private val wrapper: T?

    constructor(wrapper: T) : super(Code.OK.toString(), Code.OK.message) {
        this.wrapper = wrapper
    }

    constructor(message: String, wrapper: T) : super(Code.OK.toString(), message) {
        this.wrapper = wrapper
    }

    companion object {

        fun <T> to(message: String?, wrapper: T): ResponseData<T> {
            return ResponseData(message.orEmpty(), wrapper)
        }

        fun <T> to(message: String?): ResponseData<T?> {
            return ResponseData(message.orEmpty(), null)
        }

        fun <T> to(wrapper: T): ResponseData<T> {
            return ResponseData(wrapper)
        }
    }


}