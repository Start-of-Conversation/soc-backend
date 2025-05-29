package toyproject.startofconversation.api.card

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.card.dto.CardListResponse
import toyproject.startofconversation.api.card.dto.CardSaveRequest
import toyproject.startofconversation.api.cardGroup.exception.MaxGroupCapacityExceededException
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.cardgroup.config.CardGroupProperties
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.card.repository.CardRepository
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.exception.SOCNotFoundException

@Service
class CardService(
    private val cardRepository: CardRepository,
    private val cardGroupRepository: CardGroupRepository,
    private val properties: CardGroupProperties
) {

    fun getCards(cardGroupId: String, pageable: Pageable): PageResponseData<CardListResponse> {
        val cards = cardRepository.findAllByCardGroupId(pageable, cardGroupId)
        return PageResponseData(CardListResponse.from(cardGroupId, cards), cards)
    }

    @Transactional
    fun addCard(request: CardSaveRequest): ResponseData<Boolean> {
        val cardGroupID = request.cardGroupID
        val cardGroup = cardGroupRepository.findByIdOrNull(cardGroupID)
            ?: throw SOCNotFoundException("$cardGroupID is not found.")

        val maxSize = properties.maxSize
        if (cardRepository.countByCardGroup(cardGroup) > maxSize) {
            throw MaxGroupCapacityExceededException(maxSize)
        }

        cardRepository.save(Card(question = request.question, cardGroup = cardGroup))

        return ResponseData.to(true)
    }
}