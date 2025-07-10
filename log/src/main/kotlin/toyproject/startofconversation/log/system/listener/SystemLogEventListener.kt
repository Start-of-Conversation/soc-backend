package toyproject.startofconversation.log.system.listener

import jakarta.transaction.Transactional
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import toyproject.startofconversation.log.system.domain.entity.SystemLog
import toyproject.startofconversation.log.system.domain.repository.SystemLogRepository
import toyproject.startofconversation.log.system.service.SystemLogService

@Component
class SystemLogEventListener(
    private val systemLogService: SystemLogService
) {

    @EventListener
    @Transactional
    @Async
    fun handle(event: SystemLog) {
        systemLogService.asyncSave(event)
    }
}