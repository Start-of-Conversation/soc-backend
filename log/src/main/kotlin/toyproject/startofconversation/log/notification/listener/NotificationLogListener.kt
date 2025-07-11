package toyproject.startofconversation.log.notification.listener

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import toyproject.startofconversation.log.notification.dto.NotificationLogEvent
import toyproject.startofconversation.log.notification.service.NotificationLogService

@Component
class NotificationLogListener(
    private val notificationLogService: NotificationLogService
) {
    @EventListener
    fun handle(event: NotificationLogEvent) {
        notificationLogService.asyncSave(event)
    }
}