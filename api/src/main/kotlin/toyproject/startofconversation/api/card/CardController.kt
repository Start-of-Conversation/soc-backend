package toyproject.startofconversation.api.card

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.web.PageableDefault
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.card.dto.CardDto
import toyproject.startofconversation.api.card.dto.CardSaveRequest
import toyproject.startofconversation.api.card.dto.CardUpdateRequest
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.common.base.controller.BaseController
import toyproject.startofconversation.common.base.dto.ResponseData
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/card")
class CardController(
    private val cardService: CardService
) : BaseController() {

    @GetMapping("/public")
    fun getCardsWithFilters(
        @RequestParam("card-group") cardGroupId: String?,
        @RequestParam("from", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) from: LocalDateTime?,
        @RequestParam("to", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) to: LocalDateTime?,
        @RequestParam("user-id", required = false) userId: String?,
        @PageableDefault(size = 20, page = 0, sort = ["createdAt"], direction = DESC) pageable: Pageable
    ): PageResponseData<List<CardDto>> = cardService.findCardsWithFilter(cardGroupId, from, to, userId, pageable)

    @PostMapping("/add")
    fun addCard(
        @RequestBody request: CardSaveRequest
    ): ResponseData<CardDto> = cardService.addCard(request, getUserId())

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