package toyproject.startofconversation.api.card.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.cardgroup.config.CardGroupProperties
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.exception.MaxGroupCapacityExceededException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository

@Component
class CardGroupCapacityValidator(
    private val cardGroupRepository: CardGroupRepository,
    private val properties: CardGroupProperties
) {
    fun validate(cardGroup: CardGroup) {
        if (cardGroupRepository.countByCardGroupCardsContains(cardGroup) >= properties.maxSize) {
            throw MaxGroupCapacityExceededException(properties.maxSize)
        }
    }
}