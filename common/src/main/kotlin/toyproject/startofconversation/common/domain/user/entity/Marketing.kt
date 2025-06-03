package toyproject.startofconversation.common.domain.user.entity

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import toyproject.startofconversation.common.base.BaseEntity
import toyproject.startofconversation.common.base.value.Domain
import java.time.LocalDateTime

@Entity
@Table(name = "marketing")
class Marketing(

    var marketingConsentYn: Boolean = false,
    var marketingConsentDate: LocalDateTime? = null,
    var appPushConsentYn: Boolean = false,
    var appPushConsentDate: LocalDateTime? = null,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: Users

) : BaseEntity(Domain.MARKETING) {

    fun updateConsent(appPushYn: Boolean, marketingYn: Boolean): Marketing {
        if (appPushYn != appPushConsentYn) {
            appPushConsentYn = appPushYn
            appPushConsentDate = if (appPushYn) LocalDateTime.now() else null
        }

        if (marketingYn != marketingConsentYn) {
            marketingConsentYn = marketingYn
            marketingConsentDate = if (marketingYn) LocalDateTime.now() else null
        }

        return this
    }
}