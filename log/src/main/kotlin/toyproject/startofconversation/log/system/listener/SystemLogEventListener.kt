package toyproject.startofconversation.log.system.listener

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import toyproject.startofconversation.log.system.dto.SystemLogEvent
import toyproject.startofconversation.log.system.service.SystemLogService

@Component
class SystemLogEventListener(
    private val systemLogService: SystemLogService
) {

    @EventListener
    fun handle(event: SystemLogEvent) {
        systemLogService.asyncSave(event)
    }
}