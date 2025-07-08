package toyproject.startofconversation.api.like

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.api.paging.toPageResponse
import toyproject.startofconversation.api.user.service.UserService
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.exception.DuplicateLikeException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.domain.like.entity.Likes
import toyproject.startofconversation.common.domain.like.exception.LikeNotFoundException
import toyproject.startofconversation.common.domain.like.repository.LikesRepository
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.notification.fcm.service.FCMNotificationService
import toyproject.startofconversation.notification.fcm.util.transactionalWithNotification

@Service
@LoginUserAccess
class LikeService(
    private val userService: UserService,
    private val cardGroupRepository: CardGroupRepository,
    private val likesRepository: LikesRepository,
    private val fcmNotificationService: FCMNotificationService
) {

    /**
     * Todo
     *  클라이언트 개발 완료 시 data 부분 url 변경할 것
     */
    fun likeWithNotification(
        cardGroupId: String, userId: String
    ): ResponseData<Boolean> = transactionalWithNotification(
        block = { like(cardGroupId, userId) },
        notify = { (cardGroup, user) ->
            fcmNotificationService.sendMessageToUser(
                user = cardGroup.user,
                title = "좋아요!",
                body = "${user.nickname}님이 ${cardGroup.cardGroupName}에 좋아요를 눌렀습니다.",
                data = mapOf(
                    "type" to "cardGroup",
                    "cardGroupId" to cardGroupId,
                    "url" to "/card-group/$cardGroupId"
                )
            )
        }
    ).let {
        ResponseData.Companion.to("Successfully liked ${cardGroupId}!", true)
    }

    @Transactional
    fun like(cardGroupId: String, userId: String): Pair<CardGroup, Users> {
        val cardGroup = cardGroupRepository.findWithUserById(cardGroupId)
            ?: throw CardGroupNotFoundException(cardGroupId)
        val user = userService.findUserById(userId)

        if (likesRepository.existsByUserAndCardGroup(user, cardGroup)) {
            throw DuplicateLikeException(cardGroupId, userId)
        }

        likesRepository.save(Likes(user, cardGroup))
        return cardGroup to user
    }

    @Transactional
    fun unlike(cardGroupId: String, userId: String): ResponseData<Boolean> {
        if (!likesRepository.existsByUserIdAndCardGroupId(userId, cardGroupId)) {
            throw LikeNotFoundException(cardGroupId, userId)
        }
        likesRepository.deleteByUserIdAndCardGroupId(userId, cardGroupId)

        return ResponseData.Companion.to("Successfully unliked ${cardGroupId}!", true)
    }

    fun findCardGroupsByUser(userId: String, pageable: Pageable): PageResponseData<List<CardGroupInfoResponse>> =
        likesRepository.findByUserId(userId, pageable).toPageResponse(CardGroupInfoResponse::from)
}