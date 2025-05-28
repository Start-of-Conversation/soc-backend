package toyproject.startofconversation.api.card

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import toyproject.startofconversation.api.card.dto.CardResponse
import toyproject.startofconversation.api.card.dto.CardSaveRequest
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController
@RequestMapping("/api/card")
class CardController(
    private val cardService: CardService
) {

    /**
     * TODO
     *  - 수정 예정
     *      카드 정렬이
     *          1. 랜덤으로 할 것인지
     *          2. 아니라면 생성일순인지 수정일순인지
     *          3. 오름차순으로 할 것인지 내림차순으로 할 것인지
     *      결정 필요
     *
     */
    @GetMapping("/public/{id}")
    fun getCards(
        @PathVariable("id") cardGroupId: String,
        @PageableDefault(size = 20, page = 0, sort = ["createdAt"], direction = DESC) pageable: Pageable
    ): PageResponseData<CardResponse> =
        cardService.getCards(cardGroupId, pageable)

    @PostMapping("/public/add")
    fun addCard(@RequestBody request: CardSaveRequest): ResponseData<Boolean> = cardService.addCard(request)
}