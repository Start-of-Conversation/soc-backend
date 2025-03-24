package toyproject.startofconversation.api.paging

open class PageInfo(
    val currentPage: Int,
    val size: Int,
    val totalPages: Int,
    val totalElements: Long
) {
}