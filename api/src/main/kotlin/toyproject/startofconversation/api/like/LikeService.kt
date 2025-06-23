package toyproject.startofconversation.api.like

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.api.paging.toPageResponse
import toyproject.startofconversation.api.user.service.UserService
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.exception.DuplicateLikeException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.domain.like.entity.Likes
import toyproject.startofconversation.common.domain.like.exception.LikeNotFoundException
import toyproject.startofconversation.common.domain.like.repository.LikesRepository

@Service
@LoginUserAccess
class LikeService(
    private val userService: UserService,
    private val cardGroupRepository: CardGroupRepository,
    private val likesRepository: LikesRepository
) {

    @Transactional
    fun like(cardGroupId: String, userId: String): ResponseData<Boolean> {
        val cardGroup = cardGroupRepository.findByIdOrNull(cardGroupId) ?: throw CardGroupNotFoundException(cardGroupId)
        val user = userService.findUserById(userId)

        if (likesRepository.existsByUserAndCardGroup(user, cardGroup)) {
            throw DuplicateLikeException(cardGroupId, userId)
        }

        cardGroup.likes.add(Likes(user, cardGroup))

        return ResponseData.Companion.to("Successfully liked ${cardGroupId}!", true)
    }

    @Transactional
    fun unlike(cardGroupId: String, userId: String): ResponseData<Boolean> {
        if (!likesRepository.existsByUserIdAndCardGroupId(userId, cardGroupId)) {
            throw LikeNotFoundException(cardGroupId, userId)
        }
        likesRepository.deleteByUserIdAndCardGroupId(cardGroupId, userId)

        return ResponseData.Companion.to("Successfully unliked ${cardGroupId}!", true)
    }

    fun findCardGroupsByUser(userId: String, pageable: Pageable): PageResponseData<List<CardGroupInfoResponse>> =
        likesRepository.findByUserId(userId, pageable).toPageResponse(CardGroupInfoResponse::from)
}