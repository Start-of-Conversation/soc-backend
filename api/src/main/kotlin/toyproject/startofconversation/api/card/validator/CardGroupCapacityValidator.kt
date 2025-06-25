package toyproject.startofconversation.api.card.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.cardgroup.config.CardGroupProperties
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.exception.MaxGroupCapacityExceededException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupCardsRepository

@Component
class CardGroupCapacityValidator(
    private val cardGroupCardsRepository: CardGroupCardsRepository,
    private val properties: CardGroupProperties
) {
    fun validate(cardGroup: CardGroup) {
        if (cardGroupCardsRepository.countByCardGroup(cardGroup) >= properties.maxSize) {
            throw MaxGroupCapacityExceededException(properties.maxSize)
        }
    }
}