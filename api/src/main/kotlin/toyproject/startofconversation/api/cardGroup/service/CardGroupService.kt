package toyproject.startofconversation.api.cardGroup.service

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.cardGroup.dto.CardGroupCreateRequest
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.api.cardGroup.dto.CardGroupUpdateRequest
import toyproject.startofconversation.api.cardGroup.validator.CardGroupValidator
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.api.paging.toPageResponse
import toyproject.startofconversation.api.user.service.UserService
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository

@Service
class CardGroupService(
    private val cardGroupRepository: CardGroupRepository,
    private val userService: UserService,
    private val cardGroupValidator: CardGroupValidator
) {
    fun getCardGroupInfo(id: String): ResponseData<CardGroupInfoResponse> =
        cardGroupRepository.findByIdOrNull(id)?.let {
            ResponseData.to(CardGroupInfoResponse.from(it))
        } ?: throw CardGroupNotFoundException(id)

    fun getCardGroups(pageable: Pageable): PageResponseData<List<CardGroupInfoResponse>> =
        cardGroupRepository.findAll(pageable).toPageResponse(CardGroupInfoResponse::from)

    @Transactional
    @LoginUserAccess
    fun create(
        request: CardGroupCreateRequest, userId: String
    ): ResponseData<CardGroupInfoResponse> = with(request) {
        val user = userService.findUserById(userId)

        val cardGroup = CardGroup(
            cardGroupName = name,
            cardGroupSummary = summary,
            cardGroupDescription = description,
            isCustomized = isCustomized,
            user = user
        ).setThumbs(thumbnail)
        cardGroupRepository.save(cardGroup)

        ResponseData.to(CardGroupInfoResponse.from(cardGroup))
    }

    @Transactional
    @LoginUserAccess
    fun update(
        cardGroupId: String, request: CardGroupUpdateRequest, userId: String
    ): ResponseData<CardGroupInfoResponse> = with(request) {
        val cardGroup = cardGroupValidator.getValidCardGroupOwnedByUser(cardGroupId, userId)
            .setName(name)
            .setSummary(summary)
            .setDesc(description)
            .setThumbs(thumbnail)
            .setCustomized(isCustomized)

        ResponseData.to(CardGroupInfoResponse.from(cardGroup))
    }

    @Transactional
    @LoginUserAccess
    fun delete(cardGroupId: String, userId: String): ResponseData<Boolean> {
        val cardGroup = cardGroupRepository.findByIdAndUserId(cardGroupId, userId)
            ?: throw CardGroupNotFoundException(cardGroupId)

        cardGroupRepository.delete(cardGroup)

        return ResponseData.to("CardGroup $cardGroupId has been successfully removed.", true)
    }

}