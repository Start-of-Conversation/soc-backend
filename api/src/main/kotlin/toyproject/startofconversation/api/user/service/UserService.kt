package toyproject.startofconversation.api.user.service

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.card.dto.CardDto
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.api.paging.toPageResponse
import toyproject.startofconversation.api.user.dto.UserDataResponse
import toyproject.startofconversation.api.user.dto.UserUpdateRequest
import toyproject.startofconversation.auth.service.AuthService
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.base.dto.responseOf
import toyproject.startofconversation.common.domain.card.repository.CardRepository
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.exception.UserNotFoundException
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import java.time.LocalDateTime

@Service
class UserService(
    private val usersRepository: UsersRepository,
    private val authService: AuthService,
    private val cardRepository: CardRepository,
    private val cardGroupRepository: CardGroupRepository
) {

    @Transactional
    @LoginUserAccess
    fun deleteUser(id: String): ResponseData<Boolean> {
        val user = findUserById(id)
        if (!user.isDeleted) {
            user.isDeleted = true
            user.deletedAt = LocalDateTime.now()
        }

        authService.deleteAuth(id)

        return responseOf("Your account has been successfully deleted.", true)
    }

    @LoginUserAccess
    fun searchUserById(id: String): ResponseData<UserDataResponse> = findUserById(id).toResponse()

    @LoginUserAccess
    @Transactional
    fun updateUser(id: String, request: UserUpdateRequest): ResponseData<UserDataResponse> = findUserById(id)
        .updateNickname(request.nickname)
        .updateProfile(request.profile)
        .toResponse()


    fun findUserById(id: String): Users = usersRepository.findByIdOrNull(id)
        ?: throw UserNotFoundException(id)

    @LoginUserAccess
    fun findCardsByUserId(
        userId: String, pageable: Pageable
    ): PageResponseData<List<CardDto>> = cardRepository.findByUserId(userId, pageable)
        .toPageResponse(CardDto::from)

    @LoginUserAccess
    fun findCardGroupsByUserId(
        userId: String, pageable: Pageable
    ): PageResponseData<List<CardGroupInfoResponse>> = cardGroupRepository.findAllByUserId(userId, pageable)
        .toPageResponse(CardGroupInfoResponse::from)

    private fun Users.toResponse(): ResponseData<UserDataResponse> =
        responseOf(UserDataResponse.to(this))
}