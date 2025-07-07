package toyproject.startofconversation.notification.device.controller.dto

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import toyproject.startofconversation.common.jackson.LocalDateTimeSerializer
import java.time.LocalDateTime

data class AppPushInfoResponse(
    val pushEnabled: Boolean,

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    val pushEnabledAt: LocalDateTime?
)
