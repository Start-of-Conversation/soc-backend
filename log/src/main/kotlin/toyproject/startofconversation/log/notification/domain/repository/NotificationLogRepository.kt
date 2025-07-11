package toyproject.startofconversation.log.notification.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.log.notification.domain.entity.NotificationLog

@Repository
interface NotificationLogRepository : JpaRepository<NotificationLog, String> {
}