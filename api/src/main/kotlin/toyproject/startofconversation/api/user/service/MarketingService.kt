package toyproject.startofconversation.api.user.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.user.dto.MarketingResponse
import toyproject.startofconversation.api.user.dto.MarketingUpdateRequest
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.base.dto.responseOf
import toyproject.startofconversation.common.domain.user.entity.Marketing
import toyproject.startofconversation.common.domain.user.repository.MarketingRepository
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.exception.SOCServerException
import toyproject.startofconversation.common.logger.logger
import toyproject.startofconversation.notification.fcm.service.FCMSubscriptionService

@Service
@LoginUserAccess
class MarketingService(
    private val marketingTransactionalService: MarketingTransactionalService,
    private val fcmSubscriptionService: FCMSubscriptionService
) {

    fun getMarketingInfo(userId: String): ResponseData<MarketingResponse> = responseOf(
        marketingTransactionalService.getOrCreateMarketing(userId).toResponse()
    )

    fun updateMarketingWithFCM(userId: String, request: MarketingUpdateRequest): ResponseData<MarketingResponse> {
        val marketing = marketingTransactionalService.updateMarketingTx(userId, request)

        val shouldSubscribe = marketing.marketingConsentYn && marketing.appPushConsentYn
        if (shouldSubscribe) {
            fcmSubscriptionService.subscribeMarketing(userId)
        } else {
            fcmSubscriptionService.unsubscribeMarketing(userId)
        }

        return responseOf(marketing.toResponse())
    }

    private fun Marketing.toResponse() = MarketingResponse.from(this)
}

@Service
class MarketingTransactionalService(
    private val marketingRepository: MarketingRepository,
    private val userRepository: UsersRepository
) {
    private val log = logger()

    @Transactional
    fun updateMarketingTx(userId: String, request: MarketingUpdateRequest): Marketing {
        val marketing = getOrCreateMarketing(userId).updateConsent(
            marketingYn = request.marketingConsentYn,
            appPushYn = request.appPushConsentYn,
            emailYn = request.emailConsentYn
        )
        return marketing
    }

    @Transactional
    fun getOrCreateMarketing(userId: String): Marketing = marketingRepository.findByUserId(userId)
        ?: runCatching {
            val user = userRepository.getReferenceById(userId)
            marketingRepository.save(Marketing(user = user))
        }.onFailure {
            log.warn("마케팅 중복 생성 시도 발생: userId=$userId", it)
        }.getOrElse {
            marketingRepository.findByUserId(userId)
                ?: throw SOCServerException("중복 생성 후에도 Marketing 조회 실패")
        }
}