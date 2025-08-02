package toyproject.startofconversation.api.card

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.card.dto.CardDto
import toyproject.startofconversation.api.card.dto.CardSaveRequest
import toyproject.startofconversation.api.card.dto.CardUpdateRequest
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.card.exception.CardNotFoundException
import toyproject.startofconversation.common.domain.card.repository.CardRepository
import toyproject.startofconversation.common.domain.card.validator.CardValidator
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.support.normalize
import java.time.LocalDateTime

@Service
class CardService(
    private val cardRepository: CardRepository,
    private val userRepository: UsersRepository,
    private val validator: CardValidator
) {

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
        val normalizedQuestion = normalize(cardUpdateRequest.newQuestion)

        if (normalizedQuestion != card.question) {
            validator.validateCardDuplication(cardUpdateRequest.newQuestion)
        }

        card.updateQuestion(cardUpdateRequest.newQuestion, normalizedQuestion)
        return ResponseData.to(CardDto.from(card))
    }

    @Transactional
    @LoginUserAccess
    fun addCard(request: CardSaveRequest, userId: String): ResponseData<CardDto> = with(request) {
        val normalizedQuestion = validator.validateCardDuplication(question)
        val user = userRepository.getReferenceById(userId)
        val card = Card.from(question, user, normalizedQuestion)

        cardRepository.save(card)
        return ResponseData.to(CardDto.from(card))
    }

    @Transactional
    @LoginUserAccess
    fun deleteCard(cardId: String, userId: String): ResponseData<Boolean> {
        val deleted = cardRepository.deleteByIdAndUserId(cardId, userId)
        if (deleted == 0) throw CardNotFoundException(cardId)

        return ResponseData.to(true)
    }

}