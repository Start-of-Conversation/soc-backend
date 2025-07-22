package toyproject.startofconversation.api.paging

import org.springframework.data.domain.Page

open class PageInfo(
    val currentPage: Int,
    val size: Int,
    val totalPages: Int,
    val totalElements: Long
) {
    companion object {

        fun from(page: Page<*>): PageInfo = PageInfo(
            currentPage = page.number,
            size = page.size,
            totalPages = page.totalPages,
            totalElements = page.totalElements
        )

    }
}