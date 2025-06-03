package toyproject.startofconversation.api.user.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.user.dto.MarketingResponse
import toyproject.startofconversation.api.user.dto.MarketingUpdateRequest
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.user.entity.Marketing
import toyproject.startofconversation.common.domain.user.exception.UserNotFoundException
import toyproject.startofconversation.common.domain.user.repository.MarketingRepository

@Service
class MarketingService(
    private val marketingRepository: MarketingRepository,
    private val userService: UserService
) {

    fun getMarketingInfo(userId: String): ResponseData<MarketingResponse> =
        marketingRepository.findByUserId(userId)?.let {
            ResponseData.to(MarketingResponse.from(it))
        } ?: throw UserNotFoundException(userId)

    @Transactional
    fun updateMarketing(userId: String, request: MarketingUpdateRequest): ResponseData<Boolean> {
        getOrCreateMarketing(userId).updateConsent(
            appPushYn = request.appPushConsentYn,
            marketingYn = request.marketingConsentYn
        )
        return ResponseData(true)
    }

    private fun getOrCreateMarketing(userId: String): Marketing {
        val user = userService.findUserById(userId)
        return marketingRepository.findByUser(user)
            ?: marketingRepository.save(Marketing(user = user))
    }
}