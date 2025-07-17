package toyproject.startofconversation.api.paging

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import toyproject.startofconversation.common.base.dto.ResponseData

class PageResponseData<T> : ResponseData<T> {

    val pageInfo: PageInfo

    constructor(wrapper: T, pageInfo: PageInfo) : super(wrapper) {
        this.pageInfo = pageInfo
    }

    constructor(wrapper: T, pageObject: Page<*>) : this(wrapper, PageInfo.from(pageObject))


    constructor(message: String, wrapper: T, pageInfo: PageInfo) : super(message, wrapper) {
        this.pageInfo = pageInfo
    }

    constructor(message: String, wrapper: T, pageObject: Page<*>) :
        this(message, wrapper, PageInfo.from(pageObject))

    companion object {
        fun <T> paginate(
            items: List<T>,
            pageable: Pageable = PageRequest.of(0, 20)
        ): Page<T> {
            val start = pageable.offset.toInt()
            val end = (start + pageable.pageSize).coerceAtMost(items.size)
            val content = if (start > items.size) emptyList() else items.subList(start, end)
            return PageImpl(content, pageable, items.size.toLong())
        }
    }
}