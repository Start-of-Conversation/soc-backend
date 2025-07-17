package toyproject.startofconversation.log.notification.service

import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import toyproject.startofconversation.log.notification.domain.repository.NotificationLogRepository
import toyproject.startofconversation.log.notification.dto.NotificationLogEvent

@Service
class NotificationLogService(
    private val notificationLogRepository: NotificationLogRepository
) {
    @Async
    @Transactional
    fun asyncSave(event: NotificationLogEvent) {
        notificationLogRepository.save(event.to())
    }
}