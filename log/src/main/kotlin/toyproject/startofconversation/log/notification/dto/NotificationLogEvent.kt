package toyproject.startofconversation.log.notification.dto

import toyproject.startofconversation.common.base.value.ChannelType
import toyproject.startofconversation.common.base.value.LogLevel
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.log.notification.domain.entity.NotificationLog

data class NotificationLogEvent(
    val recipientId: String?,
    val title: String,
    val body: String,
    val channel: ChannelType,
    val isSuccess: Boolean,
    val retryCount: Int = 0,
    val errorMessage: String? = null,
    val logLevel: LogLevel
) {

    fun to(block: () -> Users?): NotificationLog = NotificationLog(
        recipient = block(),
        title = title,
        body = body,
        channel = channel,
        isSuccess = isSuccess,
        retryCount = retryCount,
        logLevel = logLevel,
        errorMessage = errorMessage
    )

    companion object {

        fun info(
            recipientId: String?,
            title: String,
            body: String,
            channel: ChannelType,
            retryCount: Int = 0
        ): NotificationLogEvent = NotificationLogEvent(
            recipientId = recipientId,
            title = title,
            body = body,
            channel = channel,
            isSuccess = true,
            retryCount = retryCount,
            errorMessage = null,
            logLevel = LogLevel.INFO
        )

        fun error(
            recipientId: String?,
            title: String,
            body: String,
            channel: ChannelType,
            retryCount: Int = 0,
            errorMessage: String?
        ): NotificationLogEvent = NotificationLogEvent(
            recipientId = recipientId,
            title = title,
            body = body,
            channel = channel,
            isSuccess = false,
            retryCount = retryCount,
            errorMessage = errorMessage,
            logLevel = LogLevel.ERROR
        )
    }
}