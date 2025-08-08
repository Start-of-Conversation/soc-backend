package toyproject.startofconversation.api.like

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.api.paging.toPageResponse
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.base.dto.responseOf
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.exception.DuplicateLikeException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.domain.like.entity.Likes
import toyproject.startofconversation.common.domain.like.repository.LikesRepository
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.notification.fcm.service.FCMNotificationService
import toyproject.startofconversation.notification.fcm.util.transactionalWithNotification

@Service
@LoginUserAccess
class LikeService(
    private val likesRepository: LikesRepository,
    private val fcmNotificationService: FCMNotificationService,
    private val likeTransactionalService: LikeTransactionalService
) {

    /**
     * Todo
     *  클라이언트 개발 완료 시 data 부분 url 변경할 것
     */
    fun likeWithNotification(
        cardGroupId: String, userId: String
    ): ResponseData<Boolean> = transactionalWithNotification(
        block = { likeTransactionalService.like(cardGroupId, userId) },
        notify = { (cardGroupOwner, cardGroupName, userName) ->
            fcmNotificationService.sendMessageToUser(
                user = cardGroupOwner,
                title = "좋아요!",
                body = "${userName}님이 ${cardGroupName}에 좋아요를 눌렀습니다.",
                data = mapOf(
                    "type" to "cardGroup",
                    "cardGroupId" to cardGroupId,
                    "url" to "/card-group/$cardGroupId"
                )
            )
        }
    ).let {
        responseOf("Successfully liked ${cardGroupId}!", true)
    }

    @Transactional
    fun unlike(cardGroupId: String, userId: String): ResponseData<Boolean> {
        likesRepository.deleteByUserIdAndCardGroupId(userId, cardGroupId)
        return responseOf("Successfully unliked ${cardGroupId}!", true)
    }

    fun findCardGroupsByUser(
        userId: String, pageable: Pageable
    ): PageResponseData<List<CardGroupInfoResponse>> = likesRepository.findLikedCardGroupsByUserId(userId, pageable)
        .toPageResponse(CardGroupInfoResponse::from)
}

@Service
class LikeTransactionalService(
    private val userRepository: UsersRepository,
    private val cardGroupRepository: CardGroupRepository,
    private val likesRepository: LikesRepository
) {
    @Transactional
    fun like(cardGroupId: String, userId: String): Triple<Users, String, String> {
        if (likesRepository.existsByUserIdAndCardGroupId(userId, cardGroupId)) {
            throw DuplicateLikeException(cardGroupId, userId)
        }

        val cardGroup = cardGroupRepository.findWithUserById(cardGroupId)
            ?: throw CardGroupNotFoundException(cardGroupId)
        val user = userRepository.getReferenceById(userId)

        likesRepository.save(Likes(user, cardGroup))
        val nickname = userRepository.findNicknameById(userId)
        return Triple(cardGroup.user, cardGroup.cardGroupName, nickname)
    }
}