package toyproject.startofconversation.notification.device.domain.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import toyproject.startofconversation.common.base.BaseDateEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.notification.device.domain.entity.value.DeviceType
import toyproject.startofconversation.common.domain.user.entity.Users
import java.time.LocalDateTime

@Table(name = "device")
@Entity
class Device(

    @Column(nullable = false, unique = true)
    @Comment("푸시 알림 토큰")
    var deviceToken: String,

    @Column(nullable = false, unique = true)
    @Comment("기기 고유값")
    val deviceId: String,

    @Enumerated(EnumType.STRING)
    @Comment("'ios', 'android', 'web'")
    val deviceType: DeviceType,

    @Comment("앱 버전(오류 시 로그용)")
    var appVersion: String,

    @Comment("기기에서 푸시 권한 동의 여부")
    var isPushEnabled: Boolean = false,
    var pushEnabledUpdatedAt: LocalDateTime? = null,

    @Comment("토큰 유효 여부")
    var isTokenValid: Boolean = true,

    @Comment("마지막 활동 시간")
    var lastSeenAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: Users

) : BaseDateEntity(Domain.DEVICE) {
    fun updateToken(deviceToken: String): Device {
        if (this.deviceToken != deviceToken) {
            this.deviceToken = deviceToken
        }
        return this
    }

    fun updateVersion(appVersion: String): Device {
        if (this.appVersion != appVersion) {
            this.appVersion = appVersion
        }
        return this
    }

    fun updateAppPushStatus(isPushEnabled: Boolean): Device {
        if (this.isPushEnabled != isPushEnabled) {
            this.isPushEnabled = isPushEnabled
            this.updatePushTimestampIfNeeded()
        }
        return this
    }

    fun updatePushTimestampIfNeeded(): Device {
        if (this.isPushEnabled) {
            pushEnabledUpdatedAt = LocalDateTime.now()
        }
        return this
    }

    fun updateLastSeenAt(): Device {
        this.lastSeenAt = LocalDateTime.now()
        return this
    }
}