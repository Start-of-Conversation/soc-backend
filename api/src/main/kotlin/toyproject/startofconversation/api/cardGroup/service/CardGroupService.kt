package toyproject.startofconversation.api.cardGroup.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.cardGroup.dto.CardGroupCreateRequest
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse.UserInfo
import toyproject.startofconversation.api.cardGroup.dto.CardGroupUpdateRequest
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.api.paging.toPageResponse
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.base.dto.responseOf
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.domain.cardgroup.validator.CardGroupValidator
import toyproject.startofconversation.common.domain.like.repository.LikesRepository
import toyproject.startofconversation.common.domain.user.repository.UsersRepository

@Service
class CardGroupService(
    private val cardGroupRepository: CardGroupRepository,
    private val userRepository: UsersRepository,
    private val cardGroupValidator: CardGroupValidator,
    private val likesRepository: LikesRepository
) {
    fun getCardGroupInfo(id: String, userId: String?): ResponseData<CardGroupInfoResponse> =
        cardGroupRepository.findCardGroupInfoById(id)?.toResponse(
            UserInfo(userId, didUserLiked(userId, id))
        ) ?: throw CardGroupNotFoundException(id)

    fun getCardGroups(pageable: Pageable): PageResponseData<List<CardGroupInfoResponse>> =
        cardGroupRepository.findCardGroupsWithCardCount(pageable).toPageResponse(CardGroupInfoResponse::from)

    @Transactional
    @LoginUserAccess
    fun create(
        request: CardGroupCreateRequest, userId: String
    ): ResponseData<CardGroupInfoResponse> = with(request) {
        val user = userRepository.getReferenceById(userId)
        cardGroupValidator.validateCustom(userId, isCustomized)

        val cardGroup = CardGroup(
            cardGroupName = name,
            cardGroupSummary = summary,
            cardGroupDescription = description,
            isCustomized = isCustomized,
            user = user
        ).setThumbs(thumbnail)
        cardGroupRepository.save(cardGroup)

        (cardGroup to 0L).toResponse()
    }

    @Transactional
    @LoginUserAccess
    fun update(
        cardGroupId: String, request: CardGroupUpdateRequest, userId: String
    ): ResponseData<CardGroupInfoResponse> = with(request) {
        val result = cardGroupValidator.getValidCardGroupWithCountOwnedByUser(cardGroupId, userId)
        isCustomized?.let { cardGroupValidator.validateCustom(userId, isCustomized) }

        result.first
            .setName(name)
            .setSummary(summary)
            .setDesc(description)
            .setThumbs(thumbnail)
            .setCustomized(isCustomized)

        result.toResponse()
    }

    @Transactional
    @LoginUserAccess
    fun delete(cardGroupId: String, userId: String): ResponseData<Boolean> {
        val cardGroup = cardGroupValidator.getValidCardGroupOwnedByUser(cardGroupId, userId)
        cardGroupRepository.delete(cardGroup)
        return responseOf("CardGroup $cardGroupId has been successfully removed.", true)
    }

    private fun didUserLiked(userId: String?, cardGroupId: String): Boolean {
        if (userId != null) {
            return likesRepository.existsByUserIdAndCardGroupId(userId, cardGroupId)
        }
        return false
    }

    private fun Pair<CardGroup, Long>.toResponse(
        userInfo: UserInfo = UserInfo.empty()
    ): ResponseData<CardGroupInfoResponse> = responseOf(CardGroupInfoResponse.from(this, userInfo))

}