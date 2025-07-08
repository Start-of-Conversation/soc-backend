package toyproject.startofconversation.api.user.dto

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import toyproject.startofconversation.common.domain.user.entity.Marketing
import toyproject.startofconversation.common.jackson.LocalDateTimeSerializer
import java.time.LocalDateTime

data class MarketingResponse(
    val userId: String,
    val marketing: ConsentStatus,
    val availableChannels: ChannelConsentDto
) {
    companion object {
        fun from(marketing: Marketing): MarketingResponse = with(marketing) {
            MarketingResponse(
                userId = user.id,
                marketing = ConsentStatus(
                    consentYn = marketingConsentYn,
                    consentDate = marketingConsentDate
                ),
                availableChannels = ChannelConsentDto(
                    appPush = ConsentStatus(
                        consentYn = appPushConsentYn,
                        consentDate = appPushConsentDate
                    ),
                    email = ConsentStatus(
                        consentYn = emailConsentYn,
                        consentDate = emailConsentDate
                    )
                )
            )
        }
    }
}

data class ChannelConsentDto(
    val appPush: ConsentStatus,
    val email: ConsentStatus
)

data class ConsentStatus(
    val consentYn: Boolean,

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    val consentDate: LocalDateTime?
)
