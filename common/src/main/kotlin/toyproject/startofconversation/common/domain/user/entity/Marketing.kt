package toyproject.startofconversation.common.domain.user.entity

import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import toyproject.startofconversation.common.base.BaseEntity
import toyproject.startofconversation.common.base.value.Domain
import java.util.*

@Entity
@Table(name = "marketing")
class Marketing(

    var marketingConsentYn: Boolean = false,
    var marketingConsentDate: Date? = null,
    var appPushConsentYn: Boolean = false,
    var appPushConsentDate: Date? = null,

    @OneToOne(mappedBy = "marketing")
    val user: Users

) : BaseEntity(Domain.MARKETING)