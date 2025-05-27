package toyproject.startofconversation.api.cardGroup

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.cardgroup.repository.CardGroupRepository
import toyproject.startofconversation.common.exception.SOCNotFoundException

@Service
class CardGroupService(
    private val cardGroupRepository: CardGroupRepository,
) {
    fun getCardGroupInfo(id: String): ResponseData<CardGroupInfoResponse> {
        val cardGroup = cardGroupRepository.findByIdOrNull(id) ?: throw SOCNotFoundException("$id is not found")
        return ResponseData.Companion.to(CardGroupInfoResponse.Companion.from(cardGroup))
    }
}