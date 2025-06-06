package toyproject.startofconversation.api.card.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.card.exception.CardDuplicationException
import toyproject.startofconversation.common.domain.card.repository.CardRepository

@Component
class CardDuplicationValidator(
    private val cardRepository: CardRepository,
) {
    fun validate(card: Card) {
        if (cardRepository.existsByNormalizedQuestion(card.normalizedQuestion)) {
            throw CardDuplicationException(card.id, card.question)
        }
    }
}