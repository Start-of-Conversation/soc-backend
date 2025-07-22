package toyproject.startofconversation.notification.device.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.exception.UserMismatchException

@Component
class DeviceValidator {

    fun validateOwnership(owner: Users, requester: Users) =
        validateOwnership(owner.id, requester.id)

    fun validateOwnership(ownerId: String, requesterId: String) {
        if (ownerId != requesterId) {
            throw UserMismatchException(requesterId)
        }
    }
}