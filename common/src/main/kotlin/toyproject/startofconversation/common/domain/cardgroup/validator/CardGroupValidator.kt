package toyproject.startofconversation.common.domain.cardgroup.validator

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.domain.user.entity.value.Role
import toyproject.startofconversation.common.domain.user.exception.UserMismatchException
import toyproject.startofconversation.common.domain.user.exception.UserNotFoundException
import toyproject.startofconversation.common.domain.user.repository.UsersRepository

@Component
class CardGroupValidator(
    private val cardGroupRepository: CardGroupRepository,
    private val userRepository: UsersRepository
) {

    fun getValidCardGroupOwnedByUser(cardGroupId: String, userId: String): CardGroup {
        val cardGroup = cardGroupRepository.findByIdOrNull(cardGroupId)
            ?: throw CardGroupNotFoundException(cardGroupId)
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException(userId)

        if (user.role != Role.ADMIN && cardGroup.user != user) {
            throw UserMismatchException(userId)
        }
        return cardGroup
    }
}