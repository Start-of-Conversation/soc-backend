package toyproject.startofconversation.log.system.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.log.system.domain.repository.SystemLogRepository
import toyproject.startofconversation.log.system.dto.SystemLogEvent

@Service
class SystemLogService(
    private val systemLogRepository: SystemLogRepository,
    private val usersRepository: UsersRepository
) {

    @Async
    @Transactional
    fun asyncSave(event: SystemLogEvent) {
        systemLogRepository.save(event.to {
            event.userId?.let(usersRepository::findByIdOrNull)
        })
    }
}