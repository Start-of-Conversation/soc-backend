package toyproject.startofconversation.log.system.service

import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import toyproject.startofconversation.log.system.domain.repository.SystemLogRepository
import toyproject.startofconversation.log.system.dto.SystemLogEvent

@Service
class SystemLogService(
    private val systemLogRepository: SystemLogRepository
) {

    @Async
    @Transactional
    fun asyncSave(event: SystemLogEvent) {
        systemLogRepository.save(event.to())
    }
}