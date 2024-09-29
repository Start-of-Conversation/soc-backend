package toyproject.startofconversation.common.domain.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import lombok.Builder
import lombok.Getter
import toyproject.startofconversation.common.base.BaseEntityWithDate
import toyproject.startofconversation.common.domain.value.Domain
import toyproject.startofconversation.common.domain.user.value.LoginChannel
import toyproject.startofconversation.common.domain.user.value.Role
import java.time.LocalDateTime

@Entity
@Table(name = "users")
@Getter
@Builder
class Users(
    @Column(length = 30) val email: String,
    password: String,
    @Column(length = 50) var nickname: String,
    profileImage: String?,
    @Column(length = 50) val role: Role = Role.USER,
    @Column(length = 50) val loginChannel: LoginChannel = LoginChannel.LOCAL,
    acceptMarketing: Boolean = false,
    acceptMarketingDate: LocalDateTime?,
) : BaseEntityWithDate(Domain.USER) {

    @Column(length = 50)
    var password = password
        protected set

    @Column(length = 50)
    var profileImage = profileImage
        protected set

    @Column(length = 50)
    var acceptMarketing = acceptMarketing
        protected set

    @Column(length = 50)
    var acceptMarketingDate = acceptMarketingDate
        protected set

}