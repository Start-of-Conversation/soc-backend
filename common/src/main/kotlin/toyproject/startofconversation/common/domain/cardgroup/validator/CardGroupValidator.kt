package toyproject.startofconversation.common.domain.cardgroup.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.domain.user.entity.value.Role
import toyproject.startofconversation.common.domain.user.exception.UserMismatchException
import toyproject.startofconversation.common.support.throwIf

@Component
class CardGroupValidator(
    private val cardGroupRepository: CardGroupRepository
) {

    fun getValidCardGroupOwnedByUser(cardGroupId: String, userId: String): CardGroup {
        val cardGroup = cardGroupRepository.findWithUserById(cardGroupId)
            ?: throw CardGroupNotFoundException(cardGroupId)

        validateOwnership(cardGroup, userId)
        return cardGroup
    }

    fun getValidCardGroupOwnedByUserWithCards(cardGroupId: String, userId: String): CardGroup {
        val cardGroup = cardGroupRepository.findWithUserAndCardsById(cardGroupId)
            ?: throw CardGroupNotFoundException(cardGroupId)

        validateOwnership(cardGroup, userId)
        return cardGroup
    }

    fun getValidCardGroupWithCountOwnedByUser(cardGroupId: String, userId: String): Pair<CardGroup, Long> {
        val (cardGroup, count) = cardGroupRepository.findCardGroupInfoById(cardGroupId)
            ?: throw CardGroupNotFoundException(cardGroupId)

        validateOwnership(cardGroup, userId)
        return cardGroup to count
    }

    private fun validateOwnership(cardGroup: CardGroup, userId: String) =
        throwIf(cardGroup.user.role != Role.ADMIN && cardGroup.user.id != userId) {
            UserMismatchException(userId)
        }
}