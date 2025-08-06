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
    fun deleteUser(id: String): ResponseData<Boolean> = usersRepository.findByIdOrNull(id)?.let {
        if (!it.isDeleted) {
            it.isDeleted = true
            it.deletedAt = LocalDateTime.now()
        }

        authService.deleteAuth(id)

        responseOf("Your account has been successfully deleted.", true)
    } ?: throw UserNotFoundException(id)

    @LoginUserAccess
    fun searchUserById(id: String): ResponseData<UserDataResponse> = usersRepository.findByIdOrNull(id)?.let {
        responseOf(UserDataResponse.to(it))
    } ?: throw UserNotFoundException(id)

    fun findUserById(id: String): Users = usersRepository.findByIdOrNull(id)
        ?: throw UserNotFoundException(id)

    fun findCardsByUserId(
        userId: String, pageable: Pageable
    ): PageResponseData<List<CardDto>> = cardRepository.findByUserId(userId, pageable)
        .toPageResponse(CardDto::from)

    fun findCardGroupsByUserId(
        userId: String, pageable: Pageable
    ): PageResponseData<List<CardGroupInfoResponse>> = cardGroupRepository.findAllByUserId(userId, pageable)
        .toPageResponse(CardGroupInfoResponse::from)
}