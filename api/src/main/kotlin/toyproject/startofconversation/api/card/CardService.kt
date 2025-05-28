package toyproject.startofconversation.api.card

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import toyproject.startofconversation.api.card.dto.CardResponse
import toyproject.startofconversation.api.card.dto.CardSaveRequest
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.card.repository.CardRepository
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository

@Service
class CardService(
    private val cardRepository: CardRepository,
    private val cardGroupRepository: CardGroupRepository
) {

    fun getCards(cardGroupId: String, pageable: Pageable): PageResponseData<CardResponse> {
        val cards = cardRepository.findAllByCardGroupId(pageable, cardGroupId)
        return PageResponseData(CardResponse.from(cardGroupId, cards), cards)
    }

    fun addCard(request: CardSaveRequest): ResponseData<Boolean> =
        ResponseData.to(true)
}