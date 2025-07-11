package toyproject.startofconversation.common.base

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import toyproject.startofconversation.common.base.value.Domain
import java.time.LocalDateTime

@MappedSuperclass
class BaseSentEntity(domain: Domain) : BaseEntity(domain) {

    @Column(name = "sent_at", updatable = true)
    var sentAt: LocalDateTime? = null

}