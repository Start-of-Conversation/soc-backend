package toyproject.startofconversation.common.domain.user.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import toyproject.startofconversation.common.base.BaseDateEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.device.entity.Device
import toyproject.startofconversation.common.domain.user.entity.value.Role
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class Users(

    @Column(length = 20, nullable = false)
    var nickname: String,

    var profile: String = "~/image/profiles/default_profile.png",

    @Enumerated(EnumType.STRING)
    val role: Role = Role.USER,

    var isDeleted: Boolean = false,

    var deletedAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "user")
    val devices: MutableSet<Device> = mutableSetOf(),

    @OneToMany(mappedBy = "user")
    val likes: MutableList<Likes> = mutableListOf()

) : BaseDateEntity(Domain.USER)