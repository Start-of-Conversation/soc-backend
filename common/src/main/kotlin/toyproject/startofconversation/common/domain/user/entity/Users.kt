package toyproject.startofconversation.common.domain.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import toyproject.startofconversation.common.base.BaseDateEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.like.entity.Likes
import toyproject.startofconversation.common.domain.user.entity.value.Role
import toyproject.startofconversation.common.domain.user.exception.DeletedUserException
import java.time.Clock
import java.time.LocalDate
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

    var deletedAt: LocalDateTime? = null

) : BaseDateEntity(Domain.USER) {

    @OneToMany(mappedBy = "user")
    var likes: MutableSet<Likes> = mutableSetOf()

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

    fun isNew(clock: Clock = Clock.systemDefaultZone()): Boolean =
        createdAt?.toLocalDate() == LocalDate.now(clock)
}