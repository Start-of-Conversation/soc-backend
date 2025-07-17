package toyproject.startofconversation.api.cardGroup.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.card.dto.CardListResponse
import toyproject.startofconversation.api.cardGroup.dto.AddCardToGroupRequest
import toyproject.startofconversation.api.cardGroup.dto.RemoveCardToGroupRequest
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroupCards
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupCardsRepository
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.domain.cardgroup.validator.CardGroupCardValidator
import toyproject.startofconversation.common.domain.cardgroup.validator.CardGroupValidator
import toyproject.startofconversation.common.lock.strategy.LockService

@Service
class CardGroupCardService(
    private val cardGroupCardsRepository: CardGroupCardsRepository,
    private val cardGroupCardValidator: CardGroupCardValidator,
    private val cardGroupRepository: CardGroupRepository,
    private val cardGroupValidator: CardGroupValidator,
    private val lockService: LockService
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
    ): PageResponseData<CardListResponse> {
        val cardGroup = cardGroupValidator.getValidCardGroupOwnedByUserWithCards(cardGroupId, userId)

        val cardsInGroup = withGroupCardLock(cardGroupId) {
            val cards = cardGroupCardValidator.filterValidCards(request.cardIds, cardGroup)
            cardGroup.cardGroupCards.addAll(cards.map { CardGroupCards(cardGroup, it) })
            cardGroup.cardGroupCards.map { it.card }
        }

        val paged = PageResponseData.paginate(cardsInGroup)
        return PageResponseData(
            CardListResponse.from(cardGroupId, paged.content),
            paged
        )
    }

    @Transactional
    @LoginUserAccess
    fun deleteCardToGroup(
        cardGroupId: String, request: RemoveCardToGroupRequest, userId: String
    ): ResponseData<CardListResponse> = with(request) {
        val cardGroup = cardGroupValidator.getValidCardGroupOwnedByUserWithCards(cardGroupId, userId)
        cardGroupCardValidator.validateCardIdsInGroup(cardIds, cardGroup)

        cardGroup.cardGroupCards.removeIf { it.card.id in cardIds }

        val cards = PageResponseData.paginate(cardGroup.cardGroupCards.map { it.card })
        return PageResponseData(CardListResponse.Companion.from(cardGroupId, cards.content), cards)
    }


    fun getCardListByCardGroup(cardGroup: CardGroup, pageable: Pageable): Page<Card> =
        cardGroupCardsRepository.findAllByCardGroup(pageable, cardGroup)
            .map { cardGroupCards -> cardGroupCards.card }

    private fun <T> withGroupCardLock(groupId: String, block: () -> T): T =
        lockService.executeWithLock(lockKey = groupCardLockKey(groupId), block = block)

    companion object {
        private fun groupCardLockKey(groupId: String) = "lock:card-group:$groupId:add-card"
    }
}