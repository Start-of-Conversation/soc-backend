package toyproject.startofconversation.log.system.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.log.system.domain.entity.SystemLog

@Repository
interface SystemLogRepository : JpaRepository<SystemLog, String> {
}