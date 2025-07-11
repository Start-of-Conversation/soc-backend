package toyproject.startofconversation.log.notification.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import toyproject.startofconversation.common.base.value.ChannelType
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.base.value.LogLevel
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.log.base.BaseLogEntity
import java.time.LocalDateTime

@Entity
@Table(name = "notification_log")
class NotificationLog(

    @Column(nullable = true)
    var sentAt: LocalDateTime? = null,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val body: String,

    @Column(nullable = false)
    var retryCount: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val channel: ChannelType,

    @Column(nullable = false)
    var isSuccess: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    var recipient: Users? = null,

    logLevel: LogLevel = LogLevel.INFO,
    errorMessage: String? = null

) : BaseLogEntity(Domain.NOTIFICATION_LOG, logLevel, errorMessage) {
}