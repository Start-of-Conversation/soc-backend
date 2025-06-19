package toyproject.startofconversation.common.domain.user.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import toyproject.startofconversation.common.base.BaseDateEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.device.entity.Device
import toyproject.startofconversation.common.domain.user.entity.value.Role
import toyproject.startofconversation.common.domain.user.exception.DeletedUserException
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class Users(

    @Column(length = 20, nullable = false)
    var nickname: String,

    var profile: String = "~/image/profiles/default_profile.png",

    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER,

    var isApproved: Boolean = when (role) {
        Role.ADMIN -> false   // 관리자: 내부 승인 필요
        else -> true          // 다른 권한일 경우: 기본 활성화
    },

    var isDeleted: Boolean = false,

    var deletedAt: LocalDateTime? = null,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    var marketing: Marketing? = null

) : BaseDateEntity(Domain.USER) {

    @OneToMany(mappedBy = "user")
    var devices: MutableSet<Device> = mutableSetOf()

    @OneToMany(mappedBy = "user")
    var likes: MutableSet<Likes> = mutableSetOf()

    fun createMarketing(): Users {
        val marketing = Marketing(user = this)
        this.marketing = marketing
        return this
    }

    fun approve(): Users {
        this.isApproved = true
        return this
    }

    fun ensureNotDeleted(): Users {
        if (isDeleted) {
            throw DeletedUserException(this.id)
        }

        return this
    }
}