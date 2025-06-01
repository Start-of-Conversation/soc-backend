package toyproject.startofconversation.api.card.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.card.repository.CardRepository
import toyproject.startofconversation.common.domain.cardgroup.config.CardGroupProperties
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.exception.MaxGroupCapacityExceededException

@Component
class CardGroupCapacityValidator(
    private val cardRepository: CardRepository,
    private val properties: CardGroupProperties
) {
    fun validate(cardGroup: CardGroup) {
        if (cardRepository.countByCardGroupsContains(cardGroup) >= properties.maxSize) {
            throw MaxGroupCapacityExceededException(properties.maxSize)
        }
    }
}