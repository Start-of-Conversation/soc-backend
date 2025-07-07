package toyproject.startofconversation.common.domain.card.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup

@Component
class CardValidator(
    private val cardGroupCapacityValidator: CardGroupCapacityValidator,
    private val cardDuplicationValidator: CardDuplicationValidator
) {

    fun validateCardGroupCapacity(cardGroup: CardGroup) = cardGroupCapacityValidator.validate(cardGroup)

    fun validateCardDuplication(card: Card) = cardDuplicationValidator.validate(card)

}