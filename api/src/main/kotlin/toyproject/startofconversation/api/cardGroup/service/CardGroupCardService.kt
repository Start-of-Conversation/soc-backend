package toyproject.startofconversation.api.cardGroup.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.card.dto.CardListResponse
import toyproject.startofconversation.api.cardGroup.dto.AddCardToGroupRequest
import toyproject.startofconversation.api.cardGroup.validator.CardGroupCardValidator
import toyproject.startofconversation.api.cardGroup.validator.CardGroupValidator
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroupCards
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupCardsRepository
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository

@Service
class CardGroupCardService(
    private val cardGroupCardsRepository: CardGroupCardsRepository,
    private val cardGroupCardValidator: CardGroupCardValidator,
    private val cardGroupRepository: CardGroupRepository,
    private val cardGroupValidator: CardGroupValidator
) {

    fun getCards(cardGroupId: String, pageable: Pageable): PageResponseData<CardListResponse> =
        cardGroupRepository.findByIdOrNull(cardGroupId)?.let {
            val cards = getCardListByCardGroup(it, pageable)
            PageResponseData(CardListResponse.from(cardGroupId, cards.content), cards)
        } ?: throw CardGroupNotFoundException(cardGroupId)

    @Transactional
    @LoginUserAccess
    fun addCardToGroup(
        cardGroupId: String, request: AddCardToGroupRequest, userId: String
    ): ResponseData<CardListResponse> = with(request) {
        val cardGroup = cardGroupValidator.getValidCardGroupOwnedByUser(cardGroupId, userId)
        val newCardGroupCards = cardGroupCardValidator.filterValidCards(cardIds, cardGroup)
            .map { CardGroupCards(cardGroup, it) }
        cardGroup.cardGroupCards.addAll(newCardGroupCards)

        val cards = getCardListByCardGroup(cardGroup, PageRequest.of(0, 20))
        PageResponseData(CardListResponse.Companion.from(cardGroupId, cards.content), cards)
    }


    fun getCardListByCardGroup(cardGroup: CardGroup, pageable: Pageable): Page<Card> =
        cardGroupCardsRepository.findAllByCardGroup(pageable, cardGroup)
            .map { cardGroupCards -> cardGroupCards.card }
}