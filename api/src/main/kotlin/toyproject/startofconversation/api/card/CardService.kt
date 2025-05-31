package toyproject.startofconversation.api.card

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.card.dto.*
import toyproject.startofconversation.api.card.validator.CardGroupCapacityValidator
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.api.user.UserService
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.card.exception.CardNotFoundException
import toyproject.startofconversation.common.domain.card.repository.CardRepository
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.lock.strategy.LockService

@Service
class CardService(
    private val cardRepository: CardRepository,
    private val cardGroupRepository: CardGroupRepository,
    private val lockService: LockService,
    private val userService: UserService,
    private val validator: CardGroupCapacityValidator
) {

    fun getCards(cardGroupId: String, pageable: Pageable): PageResponseData<CardListResponse> {
        val cardGroup = cardGroupRepository.findByIdOrNull(cardGroupId) ?: throw CardGroupNotFoundException(cardGroupId)
        val cards = cardRepository.findAllByCardGroups(pageable, cardGroup)
        return PageResponseData(CardListResponse.from(cardGroupId, cards), cards)
    }

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
        val cardDto = CardDto(card.getId(), card.question)
        return ResponseData.to(CardResponse(request.cardGroupId, cardDto))
    }

    private fun <T> withGroupCardLock(groupId: String, block: () -> T): T =
        lockService.executeWithLock(lockKey = groupCardLockKey(groupId), block = block)

    private fun createCardAndSave(cardGroupId: String, question: String, userId: String): Card {
        val cardGroup = cardGroupRepository.findByIdOrNull(cardGroupId) ?: throw CardGroupNotFoundException(cardGroupId)
        val user = userService.findUserById(userId)

        validator.validate(cardGroup)

        val card = Card.from(question = question, cardGroup = cardGroup, user = user)
        cardRepository.save(card)

        return card
    }

    companion object {
        private fun groupCardLockKey(groupId: String) = "lock:card-group:$groupId:add-card"
    }
}