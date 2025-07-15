package toyproject.startofconversation.log.system.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.base.value.LogLevel
import toyproject.startofconversation.common.base.value.ModuleType
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.log.base.BaseLogEntity
import java.time.LocalDateTime

@Entity
@Table(name = "system_log")
class SystemLog(

    @Column(nullable = false)
    var loggedAt: LocalDateTime = LocalDateTime.now(),

    @Column(length = 50)
    val ipAddress: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val module: ModuleType = ModuleType.SYSTEM,

    @Column
    val action: String,

    @Column(nullable = true, length = 50)
    var userId: String? = null,

    logLevel: LogLevel = LogLevel.INFO,
    errorMessage: String? = null

) : BaseLogEntity(Domain.SYSTEM_LOG, logLevel, errorMessage) {

}