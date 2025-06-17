package toyproject.startofconversation.api.card

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.card.dto.CardDto
import toyproject.startofconversation.api.card.dto.CardResponse
import toyproject.startofconversation.api.card.dto.CardSaveRequest
import toyproject.startofconversation.api.card.dto.CardUpdateRequest
import toyproject.startofconversation.api.card.validator.CardValidator
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.api.user.service.UserService
import toyproject.startofconversation.auth.support.AuthValidator
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.card.exception.CardNotFoundException
import toyproject.startofconversation.common.domain.card.repository.CardRepository
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.domain.user.exception.UserMismatchException
import toyproject.startofconversation.common.lock.strategy.LockService
import java.time.LocalDateTime

@Service
class CardService(
    private val cardRepository: CardRepository,
    private val cardGroupRepository: CardGroupRepository,
    private val lockService: LockService,
    private val userService: UserService,
    private val validator: CardValidator,
    private val authValidator: AuthValidator
) {

    fun findCardsByUserId(userId: String, pageable: Pageable): PageResponseData<List<CardDto>> =
        cardRepository.findByUserId(userId, pageable).run {
            PageResponseData(map(CardDto::from).toList(), this)
        }

    @Transactional(readOnly = true)
    fun findCardsWithFilter(
        cardGroupId: String?,
        from: LocalDateTime?,
        to: LocalDateTime?,
        userId: String?,
        pageable: Pageable
    ): PageResponseData<List<CardDto>> = cardRepository.findFilteredCards(
        cardGroupId, from, to, userId, pageable
    ).run {
        PageResponseData(map(CardDto::from).toList(), this)
    }

    @Transactional
    @LoginUserAccess
    fun updateCard(cardId: String, cardUpdateRequest: CardUpdateRequest, userId: String): ResponseData<CardDto> {
        val card = cardRepository.findByIdAndUserId(cardId, userId) ?: throw CardNotFoundException(cardId)
        authValidator.validateUserAccess(userId, card.user.id)

        card.updateQuestion(cardUpdateRequest.newQuestion)
        return ResponseData.to(CardDto.from(card))
    }

    @Transactional
    @LoginUserAccess
    fun addCard(request: CardSaveRequest, userId: String): ResponseData<CardResponse> {
        val card = withGroupCardLock(request.cardGroupId) {
            createCardAndSave(request.cardGroupId, request.question, userId)
        }
        val cardDto = CardDto.from(card)
        return ResponseData.to(CardResponse(request.cardGroupId, cardDto))
    }

    @Transactional
    @LoginUserAccess
    fun deleteCard(cardId: String, userId: String): ResponseData<Boolean> {
        val card = cardRepository.findByIdOrNull(cardId) ?: throw CardNotFoundException(cardId)

        authValidator.validateUserAccess(userId, card.user.id)

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