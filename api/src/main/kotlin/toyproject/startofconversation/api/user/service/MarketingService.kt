package toyproject.startofconversation.api.user.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.user.dto.MarketingUpdateRequest
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.user.entity.Marketing
import toyproject.startofconversation.common.domain.user.repository.MarketingRepository

@Service
class MarketingService(
    private val marketingRepository: MarketingRepository,
    private val userService: UserService
) {

    @Transactional
    fun updateMarketing(userId: String, request: MarketingUpdateRequest): ResponseData<Boolean> {
        getOrCreateMarketing(userId).updateConsent(
            appPushYn = request.appPushConsentYn,
            marketingYn = request.marketingConsentYn
        )
        return ResponseData(true)
    }

    fun getOrCreateMarketing(userId: String): Marketing {
        val user = userService.findUserById(userId)
        return marketingRepository.findByUser(user)
            ?: marketingRepository.save(Marketing(user = user))
    }
}