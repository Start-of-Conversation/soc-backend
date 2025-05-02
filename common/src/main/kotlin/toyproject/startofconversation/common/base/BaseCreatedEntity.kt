package toyproject.startofconversation.common.base

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import toyproject.startofconversation.common.base.value.Domain
import java.time.LocalDateTime

@MappedSuperclass
open class BaseCreatedEntity(domain: Domain) : BaseEntity(domain) {

    @CreatedDate
    @Column(name = "created_at")
    private val createdAt: LocalDateTime = LocalDateTime.now()
}