package toyproject.startofconversation.api.cardGroup.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.card.exception.SomeCardsNotFoundException
import toyproject.startofconversation.common.domain.card.repository.CardRepository
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.logger.logger

@Component
class CardGroupCardValidator(
    private val cardRepository: CardRepository,
) {
    private val log = logger()

    fun filterValidCards(
        requestedIds: List<String>,
        cardGroup: CardGroup
    ): List<Card> {
        val existingIds = cardGroup.cardGroupCards.map { it.card.id }.toSet()
        val foundCards = cardRepository.findAllByIdIn(requestedIds)
        val foundIds = foundCards.map { it.id }.toSet()

        validateMissingCards(foundIds, requestedIds)
        val duplicateIds = foundIds.intersect(existingIds)
        if (duplicateIds.isNotEmpty()) {
            log.info("Skipping already in cards: $duplicateIds")
        }

        return foundCards.filterNot { it.id in existingIds }
    }

    private fun validateMissingCards(foundIds: Set<String>, requestedIds: List<String>) =
        requestedIds.filterNot(foundIds::contains)
            .takeIf { it.isNotEmpty() }
            ?.let { throw SomeCardsNotFoundException(it) }

}