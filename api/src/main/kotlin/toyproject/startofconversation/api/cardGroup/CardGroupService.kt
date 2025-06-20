package toyproject.startofconversation.api.cardGroup

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.card.dto.CardListResponse
import toyproject.startofconversation.api.cardGroup.dto.CardGroupCreateRequest
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.api.user.service.UserService
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.exception.DuplicateLikeException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupCardsRepository
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.domain.user.entity.Likes
import toyproject.startofconversation.common.domain.user.repository.LikesRepository

@Service
class CardGroupService(
    private val cardGroupRepository: CardGroupRepository,
    private val cardGroupCardsRepository: CardGroupCardsRepository,
    private val likesRepository: LikesRepository,
    private val userService: UserService
) {
    fun getCardGroupInfo(id: String): ResponseData<CardGroupInfoResponse> =
        cardGroupRepository.findByIdOrNull(id)?.let {
            ResponseData.to(CardGroupInfoResponse.from(it))
        } ?: throw CardGroupNotFoundException(id)

    fun getCards(cardGroupId: String, pageable: Pageable): PageResponseData<CardListResponse> =
        cardGroupRepository.findByIdOrNull(cardGroupId)?.let {
            val cards = cardGroupCardsRepository.findAllByCardGroup(pageable, it)
                .map { cardGroupCards -> cardGroupCards.card }
            PageResponseData(CardListResponse.from(cardGroupId, cards.content), cards)
        } ?: throw CardGroupNotFoundException(cardGroupId)

    fun getCardGroups(pageable: Pageable): PageResponseData<List<CardGroupInfoResponse>> =
        toPageResponse(cardGroupRepository.findAll(pageable))

    fun getCardGroupsByUserId(userId: String, pageable: Pageable): PageResponseData<List<CardGroupInfoResponse>> =
        toPageResponse(cardGroupRepository.findAllByUserId(userId, pageable))

    @Transactional
    @LoginUserAccess
    fun like(cardGroupId: String, userId: String): ResponseData<Boolean> {
        val cardGroup = cardGroupRepository.findByIdOrNull(cardGroupId)
            ?: throw CardGroupNotFoundException(cardGroupId)
        val user = userService.findUserById(userId)

        if (likesRepository.existsByUserAndCardGroup(user, cardGroup)) {
            throw DuplicateLikeException(cardGroupId, userId)
        }

        cardGroup.likes.add(Likes(user, cardGroup))

        return ResponseData.to("Successfully liked ${cardGroupId}!", true)
    }

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
    fun delete(cardGroupId: String, userId: String): ResponseData<Boolean> {
        val cardGroup = cardGroupRepository.findByIdAndUserId(cardGroupId, userId)
            ?: throw CardGroupNotFoundException(cardGroupId)

        cardGroupRepository.delete(cardGroup)

        return ResponseData.to("CardGroup $cardGroupId has been successfully removed.", true)
    }

    private fun toPageResponse(data: Page<CardGroup>): PageResponseData<List<CardGroupInfoResponse>> =
        PageResponseData(data.map(CardGroupInfoResponse::from).toList(), data)
}