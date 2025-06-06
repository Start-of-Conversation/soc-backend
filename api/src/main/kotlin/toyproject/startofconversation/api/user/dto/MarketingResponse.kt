package toyproject.startofconversation.api.user.dto

import toyproject.startofconversation.common.domain.user.entity.Marketing
import java.time.LocalDateTime

data class MarketingResponse(val userId: String, val marketingConsent: MarketingDto) {
    companion object {
        fun from(marketing: Marketing): MarketingResponse = with(marketing) {
            MarketingResponse(
                userId = user.id,
                marketingConsent = MarketingDto(
                    appPushYn = appPushConsentYn,
                    appPushDate = appPushConsentDate,
                    marketingYn = marketingConsentYn,
                    marketingDate = marketingConsentDate
                )
            )
        }
    }
}

data class MarketingDto(
    val appPushYn: Boolean,
    val appPushDate: LocalDateTime?,
    val marketingYn: Boolean,
    val marketingDate: LocalDateTime?
)
