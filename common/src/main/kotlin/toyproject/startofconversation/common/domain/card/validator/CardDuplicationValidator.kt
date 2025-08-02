package toyproject.startofconversation.common.domain.card.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.card.exception.CardDuplicationException
import toyproject.startofconversation.common.domain.card.repository.CardRepository
import toyproject.startofconversation.common.support.normalize
import toyproject.startofconversation.common.support.throwIf

@Component
class CardDuplicationValidator(
    private val cardRepository: CardRepository,
) {
    fun validate(question: String): String = normalize(question).also {
        throwIf(cardRepository.existsByNormalizedQuestion(it)) {
            CardDuplicationException(question)
        }
    }
}