package toyproject.startofconversation.common.domain.user.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import toyproject.startofconversation.common.base.BaseEntity
import toyproject.startofconversation.common.base.value.Domain
import java.time.LocalDateTime

@Entity
@Table(name = "marketing")
class Marketing(

    @Comment("마케팅 수신 동의 여부")
    var marketingConsentYn: Boolean = false,
    var marketingConsentDate: LocalDateTime? = null,

    @Comment("앱 푸시 수신 동의 여부")
    var appPushConsentYn: Boolean = false,
    var appPushConsentDate: LocalDateTime? = null,

    @Comment("이메일 수신 여부")
    var emailConsentYn: Boolean = false,
    var emailConsentDate: LocalDateTime? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    val user: Users,

    ) : BaseEntity(Domain.MARKETING) {

    fun updateConsent(marketingYn: Boolean, appPushYn: Boolean, emailYn: Boolean): Marketing {
        if (marketingYn != marketingConsentYn) {
            marketingConsentYn = marketingYn
            marketingConsentDate = if (marketingConsentYn) LocalDateTime.now() else null
        }

        if (appPushYn != appPushConsentYn) {
            appPushConsentYn = appPushYn
            appPushConsentDate = if (appPushConsentYn) LocalDateTime.now() else null
        }

        if (emailYn != emailConsentYn) {
            emailConsentYn = emailYn
            emailConsentDate = if (emailConsentYn) LocalDateTime.now() else null
        }

        return this
    }
}