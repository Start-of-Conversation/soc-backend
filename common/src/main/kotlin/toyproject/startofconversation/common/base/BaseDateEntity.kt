package toyproject.startofconversation.common.base

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.LastModifiedDate
import toyproject.startofconversation.common.base.value.Domain
import java.time.LocalDateTime

@MappedSuperclass
open class BaseDateEntity(domain: Domain) : BaseCreatedEntity(domain) {

    @LastModifiedDate
    @Column(updatable = true, name = "updated_at")
    private val updatedAt: LocalDateTime = LocalDateTime.now()

}