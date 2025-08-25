package toyproject.startofconversation.common.domain.card.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.cardgroup.config.CardGroupProperties
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.exception.MaxCustomGroupCapacityExceededException
import toyproject.startofconversation.common.domain.cardgroup.exception.MaxGroupCapacityExceededException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupCardsRepository
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.support.throwIf

@Component
class CardGroupCapacityValidator(
    private val cardGroupCardsRepository: CardGroupCardsRepository,
    private val cardGroupRepository: CardGroupRepository,
    private val properties: CardGroupProperties
) {
    fun validate(cardGroup: CardGroup) =
        throwIf(cardGroupCardsRepository.countByCardGroup(cardGroup) >= properties.card.maxCount) {
            MaxGroupCapacityExceededException(properties.card.maxCount)
        }

    fun validateCustom(userId: String, isCustomized: Boolean) =
        throwIf(isCustomized && hasExceededCustomGroupLimit(userId)) {
            MaxCustomGroupCapacityExceededException(properties.custom.maxCount)
        }

    private fun hasExceededCustomGroupLimit(userId: String): Boolean =
        cardGroupRepository.countByUserIdAndCustomized(userId, true) >= properties.custom.maxCount
}