package toyproject.startofconversation.common.domain.card.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.card.exception.CardDuplicationException
import toyproject.startofconversation.common.domain.card.repository.CardRepository
import toyproject.startofconversation.common.support.normalize

@Component
class CardDuplicationValidator(
    private val cardRepository: CardRepository,
) {
    fun validate(question: String): String {
        val normalizedQuestion = normalize(question)
        if (cardRepository.existsByNormalizedQuestion(normalizedQuestion)) {
            throw CardDuplicationException(question)
        }
        return normalizedQuestion
    }
}