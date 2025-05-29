package toyproject.startofconversation.api.card

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.card.dto.CardDto
import toyproject.startofconversation.api.card.dto.CardListResponse
import toyproject.startofconversation.api.card.dto.CardResponse
import toyproject.startofconversation.api.card.dto.CardSaveRequest
import toyproject.startofconversation.api.cardGroup.exception.MaxGroupCapacityExceededException
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.card.repository.CardRepository
import toyproject.startofconversation.common.domain.cardgroup.config.CardGroupProperties
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.exception.SOCNotFoundException
import toyproject.startofconversation.common.lock.config.LockProperties
import toyproject.startofconversation.common.lock.strategy.LockService

@Service
class CardService(
    private val cardRepository: CardRepository,
    private val cardGroupRepository: CardGroupRepository,
    private val properties: CardGroupProperties,
    private val lockService: LockService,
    private val lockProperties: LockProperties
) {

    fun getCards(cardGroupId: String, pageable: Pageable): PageResponseData<CardListResponse> {
        val cards = cardRepository.findAllByCardGroupId(pageable, cardGroupId)
        return PageResponseData(CardListResponse.from(cardGroupId, cards), cards)
    }

    @Transactional
    fun addCard(request: CardSaveRequest): ResponseData<CardResponse> {
        val card = withGroupCardLock(request.cardGroupID) {
            createCardAndSave(request.cardGroupID, request.question)
        }
        val cardDto = CardDto(card.getId(), card.question)
        return ResponseData.to(CardResponse(request.cardGroupID, cardDto))
    }

    private fun <T> withGroupCardLock(groupId: String, block: () -> T): T = lockService.executeWithLock(
        lockKey = "lock:card-group:$groupId:add-card",
        timeout = lockProperties.timeoutMillis, block
    )

    private fun createCardAndSave(cardGroupId: String, question: String): Card {
        val cardGroup = cardGroupRepository.findByIdOrNull(cardGroupId)
            ?: throw SOCNotFoundException("$cardGroupId is not found.")

        val maxSize = properties.maxSize
        if (cardRepository.countByCardGroup(cardGroup) > maxSize) {
            throw MaxGroupCapacityExceededException(maxSize)
        }

        val card = Card(question = question, cardGroup = cardGroup)
        cardRepository.save(card)

        return card
    }
}