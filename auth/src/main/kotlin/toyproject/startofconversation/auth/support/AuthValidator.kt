package toyproject.startofconversation.auth.support

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.entity.value.Role
import toyproject.startofconversation.common.domain.user.exception.UserMismatchException
import toyproject.startofconversation.common.domain.user.exception.UserNotFoundException
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.exception.SOCForbiddenException
import toyproject.startofconversation.common.logger.logger

@Component
class AuthValidator(
    private val usersRepository: UsersRepository
) {

    private val log = logger()

    /**
     * 요청한 사용자와 현재 로그인한 사용자가 일치하는지 검증합니다.
     * 일치하지 않으면 접근 권한 예외를 발생시킵니다.
     */
    fun validateUserAccess(userId: String, currentUserId: String) {
        if (userId != currentUserId) {
            log.info("userId: {}, currentUserId: {}", userId, currentUserId)
            throw UserMismatchException(currentUserId)
        }
    }

    /**
     * ID로 사용자를 조회하고, 관리자 권한인지 검증합니다.
     */
    fun validateAdmin(userId: String) = validateAdmin(findUser(userId))

    /**
     * 주어진 사용자가 관리자 권한인지 검증합니다.
     */
    fun validateAdmin(user: Users) {
        if (user.role != Role.ADMIN) {
            throw SOCForbiddenException("Only administrators are allowed to access this resource.")
        }
    }

    /**
     * 인증된 사용자인지
     */
    fun validateApprovedUser(user: Users) {
        if (!user.isApproved) {
            throw SOCForbiddenException("Only approved users are allowed to access this resource.")
        }
    }

    fun validateApprovedAdmin(userId: String) = validateApprovedAdmin(findUser(userId))

    fun validateApprovedAdmin(user: Users) {
        validateAdmin(user)
        validateApprovedUser(user)
    }

    private fun findUser(userId: String): Users = usersRepository.findUserById(userId)
        ?: throw UserNotFoundException("User with ID $userId not found.")

}