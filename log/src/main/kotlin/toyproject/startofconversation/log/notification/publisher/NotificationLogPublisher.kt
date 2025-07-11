package toyproject.startofconversation.log.notification.publisher

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import toyproject.startofconversation.common.base.value.ChannelType
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.log.notification.dto.NotificationLogEvent

@Service
class NotificationLogPublisher(
    private val eventPublisher: ApplicationEventPublisher
) {

    fun withNotificationLog(
        recipient: Users?,
        title: String,
        body: String,
        channel: ChannelType,
        retryCount: Int = 0,
        block: () -> Unit
    ) {
        withNotificationLog(recipient?.id, title, body, channel, retryCount, block)
    }

    fun withNotificationLog(
        recipientId: String?,
        title: String,
        body: String,
        channel: ChannelType,
        retryCount: Int = 0,
        block: () -> Unit
    ) {
        try {
            block()

            eventPublisher.publishEvent(
                NotificationLogEvent.info(
                    recipientId = recipientId,
                    title = title,
                    body = body,
                    channel = channel,
                    retryCount = retryCount
                )
            )
        } catch (e: Exception) {
            eventPublisher.publishEvent(
                NotificationLogEvent.error(
                    recipientId = recipientId,
                    title = title,
                    body = body,
                    channel = channel,
                    retryCount = retryCount,
                    errorMessage = e.message
                )
            )
            throw e
        }
    }
}