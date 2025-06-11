package toyproject.startofconversation.auth.domain.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import org.hibernate.annotations.Comment
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.common.base.BaseCreatedEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.user.entity.Users

@Table(name = "user_auth")
@Entity
class Auth(

    @field:Email(message = "이메일 형식이 올바르지 않습니다.")
    @Column(length = 30, nullable = false, unique = true)
    val email: String,

    @Column(length = 255, nullable = true)
    var password: String? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: Users,

    @Enumerated(EnumType.STRING)
    @Comment("소셜 로그인 제공자 ('google', 'kakao', 'naver' 등)")
    val authProvider: AuthProvider,

    @Column(length = 100)
    @Comment("소셜 로그인 제공자의 고유 ID (google, kakao, naver 등)")
    val authId: String? = null

) : BaseCreatedEntity(Domain.AUTH) {

    val activeUser: Users
        get() = user.ensureNotDeleted()
}