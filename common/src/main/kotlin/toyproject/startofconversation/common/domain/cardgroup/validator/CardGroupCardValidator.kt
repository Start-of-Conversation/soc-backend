package toyproject.startofconversation.common.domain.cardgroup.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.card.exception.SomeCardsNotFoundException
import toyproject.startofconversation.common.domain.card.repository.CardRepository
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.exception.SomeCardsNotFoundInGroupException
import toyproject.startofconversation.common.logger.logger

@Component
class CardGroupCardValidator(
    private val cardRepository: CardRepository
) {
    private val log = logger()

    fun filterValidCards(requestedIds: List<String>, cardGroup: CardGroup): List<Card> = filterCardsByGroup(
        requestedIds,
        cardGroup,
        filterCondition = { found, existing -> found - existing },
        logMessage = { skipped -> "Skipping already in cards: $skipped" }
    )

    fun filterRemovalCards(requestedIds: List<String>, cardGroup: CardGroup): List<Card> = filterCardsByGroup(
        requestedIds,
        cardGroup,
        filterCondition = { found, existing -> found.intersect(existing) },
        logMessage = { skipped -> "Skipping cards not in group: $skipped" }
    )

    fun validateCardIdsInGroup(requestedIds: List<String>, cardGroup: CardGroup) {
        val existingIds = cardGroup.cardGroupCards.map { it.card.id }.toSet()

        val invalidIds = requestedIds.filterNot { it in existingIds }
        if (invalidIds.isNotEmpty()) {
            throw SomeCardsNotFoundInGroupException(invalidIds)
        }
    }

    private fun filterCardsByGroup(
        requestedIds: List<String>,
        cardGroup: CardGroup,
        filterCondition: (Set<String>, Set<String>) -> Set<String>,
        logMessage: (Set<String>) -> String
    ): List<Card> {
        val existingIds = cardGroup.cardGroupCards.map { it.card.id }.toSet()
        val foundCards = cardRepository.findAllByIdIn(requestedIds)
        val foundIds = foundCards.map { it.id }.toSet()

        validateMissingCards(foundIds, requestedIds)

        val targetIds = filterCondition(foundIds, existingIds)
        if (targetIds.size != foundCards.size) {
            log.info(logMessage(foundIds.subtract(targetIds)))
        }

        return foundCards.filter { it.id in targetIds }
    }

    private fun validateMissingCards(foundIds: Set<String>, requestedIds: List<String>) =
        requestedIds.filterNot(foundIds::contains)
            .takeIf { it.isNotEmpty() }
            ?.let { throw SomeCardsNotFoundException(it) }

}