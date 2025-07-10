package toyproject.startofconversation.log.system.service

import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import toyproject.startofconversation.log.system.domain.entity.SystemLog
import toyproject.startofconversation.log.system.domain.repository.SystemLogRepository

@Service
class SystemLogService(
    private val systemLogRepository: SystemLogRepository
) {

    @Async
    @Transactional
    fun asyncSave(systemLog: SystemLog) {
        systemLogRepository.save(systemLog)
    }
}