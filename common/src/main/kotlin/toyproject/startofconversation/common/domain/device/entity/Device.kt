package toyproject.startofconversation.common.domain.device.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import toyproject.startofconversation.common.base.BaseDateEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.device.entity.value.DeviceType
import toyproject.startofconversation.common.domain.user.entity.Users
import java.time.LocalDateTime

@Table(name = "device")
@Entity
class Device(

    @Column(nullable = false, unique = true)
    @Comment("푸시 알림 토큰")
    val deviceToken: String,

    @Column(nullable = false, unique = true)
    @Comment("기기 고유값")
    val deviceId: String,

    @Enumerated(EnumType.STRING)
    @Comment("'ios', 'android', 'web'")
    val deviceType: DeviceType,

    var isActive: Boolean = false,

    var lastSeenAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: Users

) : BaseDateEntity(Domain.DEVICE)