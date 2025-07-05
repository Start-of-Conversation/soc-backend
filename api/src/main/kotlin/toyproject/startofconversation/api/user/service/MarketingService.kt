package toyproject.startofconversation.api.user.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.user.dto.MarketingResponse
import toyproject.startofconversation.api.user.dto.MarketingUpdateRequest
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.user.entity.Marketing
import toyproject.startofconversation.common.domain.user.exception.UserNotFoundException
import toyproject.startofconversation.common.domain.user.repository.MarketingRepository
import toyproject.startofconversation.notification.fcm.FCMService

@Service
@LoginUserAccess
class MarketingService(
    private val marketingRepository: MarketingRepository,
    private val userService: UserService,
    private val fcmService: FCMService
) {

    fun getMarketingInfo(userId: String): ResponseData<MarketingResponse> =
        marketingRepository.findByUserId(userId)?.let {
            ResponseData.to(MarketingResponse.from(it))
        } ?: throw UserNotFoundException(userId)

    @Transactional
    fun updateMarketing(userId: String, request: MarketingUpdateRequest): MarketingResponse {
        val marketing = getOrCreateMarketing(userId).updateConsent(
            marketingYn = request.marketingConsentYn,
            appPushYn = request.appPushConsentYn,
            emailYn = request.emailConsentYn
        )

        return MarketingResponse.from(marketing)
    }

    fun updateMarketingWithFCM(userId: String, request: MarketingUpdateRequest): ResponseData<MarketingResponse> {
        val response = updateMarketing(userId, request)

        val shouldSubscribe = response.marketing.consentYn && response.availableChannels.appPush.consentYn
        if (shouldSubscribe) {
            fcmService.subscribeMarketing(userId)
        } else {
            fcmService.unsubscribeMarketing(userId)
        }

        return ResponseData(response)
    }

    private fun getOrCreateMarketing(userId: String): Marketing {
        val user = userService.findUserById(userId)
        return marketingRepository.findByUser(user)
            ?: marketingRepository.save(Marketing(user = user))
    }
}