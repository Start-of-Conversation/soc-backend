package toyproject.startofconversation.api.paging

import org.springframework.data.domain.Page
import toyproject.startofconversation.common.base.dto.ResponseData

class PageResponseData<T> : ResponseData<T> {

    private val pageInfo: PageInfo

    constructor(wrapper: T, pageInfo: PageInfo) : super(wrapper) {
        this.pageInfo = pageInfo
    }

    constructor(wrapper: T, pageObject: Page<*>) : this(wrapper, PageInfo.from(pageObject))


    constructor(message: String, wrapper: T, pageInfo: PageInfo) : super(message, wrapper) {
        this.pageInfo = pageInfo
    }

    constructor(message: String, wrapper: T, pageObject: Page<*>) :
        this(message, wrapper, PageInfo.from(pageObject))

}