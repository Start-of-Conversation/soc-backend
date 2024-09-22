package toyproject.startofconversation.common.domain.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import lombok.Getter
import toyproject.startofconversation.common.domain.common.BaseEntity
import toyproject.startofconversation.common.domain.common.value.Domain
import toyproject.startofconversation.common.domain.user.value.LoginChannel
import toyproject.startofconversation.common.domain.user.value.Role

@Entity
@Table(name = "users")
@Getter
class Users(
    @Column(length = 30) val email: String,
    @Column(length = 50) var password: String,
    @Column(length = 50) var nickname: String,
    @Column(length = 50) var profileImage: String?,
    @Column(length = 50) var role: Role,
    @Column(length = 50) var loginChannel: LoginChannel,
) : BaseEntity(Domain.USER) {

}