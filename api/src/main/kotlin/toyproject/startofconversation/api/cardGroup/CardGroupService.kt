package toyproject.startofconversation.api.cardGroup

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.cardgroup.exception.CardGroupNotFoundException
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository

@Service
class CardGroupService(
    private val cardGroupRepository: CardGroupRepository,
) {
    fun getCardGroupInfo(id: String): ResponseData<CardGroupInfoResponse> =
        cardGroupRepository.findByIdOrNull(id)?.let {
            ResponseData.to(CardGroupInfoResponse.from(it))
        } ?: throw CardGroupNotFoundException(id)
}