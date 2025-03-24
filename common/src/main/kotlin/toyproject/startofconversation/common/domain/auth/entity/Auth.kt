package toyproject.startofconversation.common.domain.auth.entity

import jakarta.persistence.*
import lombok.Builder
import lombok.Getter
import org.hibernate.annotations.Comment
import toyproject.startofconversation.common.base.BaseCreatedEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.auth.entity.value.AuthProvider
import toyproject.startofconversation.common.domain.user.entity.Users

@Getter
@Builder
@Table(name = "user_auth")
@Entity
class Auth(

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: Users,

    @Enumerated(EnumType.STRING)
    @Comment("소셜 로그인 제공자 ('google', 'kakao', 'naver' 등)")
    val authProvider: AuthProvider,

    @Column(nullable = false, length = 100)
    @Comment("소셜 로그인 제공자의 고유 ID (google, kakao, naver 등)")
    val authId: String

) : BaseCreatedEntity(Domain.AUTH)