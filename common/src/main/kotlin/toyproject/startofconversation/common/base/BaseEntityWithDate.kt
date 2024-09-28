package toyproject.startofconversation.common.base

import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import toyproject.startofconversation.common.domain.value.Domain
import java.time.LocalDateTime

@MappedSuperclass
open class BaseEntityWithDate(domain: Domain) : BaseEntity(domain) {

    @CreatedDate
    private val createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    private val modifiedAt: LocalDateTime = LocalDateTime.now()

}