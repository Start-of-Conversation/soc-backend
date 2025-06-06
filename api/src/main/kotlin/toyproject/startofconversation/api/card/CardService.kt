package toyproject.startofconversation.api.card

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.card.dto.*
import toyproject.startofconversation.api.card.validator.CardValidator
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.api.user.service.UserService
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.card.exception.CardDuplicationException
import toyproject.startofconversation.common.domain.card.exception.CardNotFoundException
import toyproject.startofconversation.common.domain.card.repository.CardRepository
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupCardsRepository
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.domain.user.exception.UserMismatchException
import toyproject.startofconversation.common.lock.strategy.LockService

@Service
class CardService(
    private val cardRepository: CardRepository,
    private val cardGroupRepository: CardGroupRepository,
    private val cardGroupCardsRepository: CardGroupCardsRepository,
    private val lockService: LockService,
    private val userService: UserService,
    private val validator: CardValidator
) {

    fun getCards(cardGroupId: String, pageable: Pageable): PageResponseData<CardListResponse> =
        cardGroupRepository.findByIdOrNull(cardGroupId)?.let {
            val cards = cardGroupCardsRepository.findAllByCardGroup(pageable, it)
                .map { cardGroupCards -> cardGroupCards.card }
            PageResponseData(CardListResponse.from(cardGroupId, cards.content), cards)
        } ?: throw CardGroupNotFoundException(cardGroupId)

    @Transactional
    fun updateCard(cardId: String, cardUpdateRequest: CardUpdateRequest): ResponseData<CardDto> {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw CardNotFoundException(cardId)
        card.updateQuestion(cardUpdateRequest.newQuestion)
        return ResponseData.to(CardDto(cardId = cardId, question = card.question))
    }

    @Transactional
    fun addCard(request: CardSaveRequest, userId: String): ResponseData<CardResponse> {
        val card = withGroupCardLock(request.cardGroupId) {
            createCardAndSave(request.cardGroupId, request.question, userId)
        }
        val cardDto = CardDto(card.id, card.question)
        return ResponseData.to(CardResponse(request.cardGroupId, cardDto))
    }

    @Transactional
    fun deleteCard(cardId: String, userId: String): ResponseData<Boolean> {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw CardNotFoundException(cardId)
        val user = userService.findUserById(userId)

        if (card.user != user) {
            throw UserMismatchException(userId);
        }

        cardRepository.deleteById(cardId)

        return ResponseData.to(true)
    }

    private fun <T> withGroupCardLock(groupId: String, block: () -> T): T =
        lockService.executeWithLock(lockKey = groupCardLockKey(groupId), block = block)

    private fun createCardAndSave(cardGroupId: String, question: String, userId: String): Card {
        val cardGroup = cardGroupRepository.findByIdOrNull(cardGroupId) ?: throw CardGroupNotFoundException(cardGroupId)
        val user = userService.findUserById(userId)

        validator.validateCardGroupCapacity(cardGroup)

        val card = Card.from(question = question, cardGroup = cardGroup, user = user)
        validator.validateCardDuplication(card)

        cardRepository.save(card)

        return card
    }

    companion object {
        private fun groupCardLockKey(groupId: String) = "lock:card-group:$groupId:add-card"
    }
}