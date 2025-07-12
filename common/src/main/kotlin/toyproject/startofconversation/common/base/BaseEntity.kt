package toyproject.startofconversation.common.base

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.support.UUIDUtil
import java.io.Serializable

@MappedSuperclass
open class BaseEntity : Serializable {

    @Id
    @Column(name = "id", updatable = false, unique = true, nullable = false, length = 50)
    lateinit var id: String
        protected set

    protected constructor()

    protected constructor(domain: Domain) {
        id = UUIDUtil.createId(domain)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is BaseEntity) return false
        return this.id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "${this::class.simpleName}(id=$id)"

}