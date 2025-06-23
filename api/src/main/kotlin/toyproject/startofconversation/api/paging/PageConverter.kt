package toyproject.startofconversation.api.paging

import org.springframework.data.domain.Page

fun <T, R> Page<T>.toPageResponse(mapper: (T) -> R): PageResponseData<List<R>> {
    return PageResponseData(this.map(mapper).toList(), this)
}
