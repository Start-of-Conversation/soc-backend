package toyproject.startofconversation.api.paging

import toyproject.startofconversation.common.base.ResponseData

class PageResponseData<T> : ResponseData<T> {

    private val pageInfo: PageInfo

    constructor(wrapper: T, pageInfo: PageInfo): super(wrapper) {
        this.pageInfo = pageInfo
    }

    constructor(message: String, wrapper: T, pageInfo: PageInfo): super(message, wrapper) {
        this.pageInfo = pageInfo
    }

}