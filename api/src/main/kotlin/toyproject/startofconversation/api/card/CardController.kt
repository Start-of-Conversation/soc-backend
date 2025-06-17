package toyproject.startofconversation.api.card

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.web.PageableDefault
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import toyproject.startofconversation.api.card.dto.*
import toyproject.startofconversation.api.common.BaseController
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.common.base.dto.ResponseData
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/card")
class CardController(
    private val cardService: CardService
) : BaseController() {

    @GetMapping("/public")
    fun getCardsByCardGroup(
        @RequestParam("id") cardGroupId: String,
        @PageableDefault(size = 20, page = 0, sort = ["createdAt"], direction = DESC) pageable: Pageable
    ): PageResponseData<CardListResponse> = cardService.getCards(cardGroupId, pageable)

    @GetMapping("filter")
    fun getCardsWithFilters(
        @RequestParam("card-group") cardGroupId: String,
        @RequestParam("from", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) from: LocalDateTime?,
        @RequestParam("to", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) to: LocalDateTime?,
        @RequestParam("userId", required = false) userId: String?,
        @PageableDefault(size = 20, page = 0, sort = ["createdAt"], direction = DESC) pageable: Pageable
    ): PageResponseData<List<CardDto>> = cardService.findCardsWithFilter(cardGroupId, from, to, userId, pageable)

    @PostMapping("/add")
    fun addCard(
        @RequestBody request: CardSaveRequest
    ): ResponseData<CardResponse> = cardService.addCard(request, getUserId())

    @PatchMapping("/{id}")
    fun updateCard(
        @PathVariable("id") cardId: String,
        @RequestBody request: CardUpdateRequest
    ): ResponseData<CardDto> = cardService.updateCard(cardId, request, getUserId())

    @DeleteMapping("/{id}")
    fun deleteCard(
        @PathVariable("id") cardId: String
    ): ResponseData<Boolean> = cardService.deleteCard(cardId, getUserId())
}