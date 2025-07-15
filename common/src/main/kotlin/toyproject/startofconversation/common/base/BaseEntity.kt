package toyproject.startofconversation.common.base

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PostPersist
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.domain.Persistable
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.exception.SOCServerException
import toyproject.startofconversation.common.support.UUIDUtil
import java.io.Serializable

@MappedSuperclass
abstract class BaseEntity : Persistable<String>, Serializable {

    @Id
    @Column(name = "id", updatable = false, unique = true, nullable = false, length = 50)
    private lateinit var id: String

    @Transient
    private var _isNew = true

    protected constructor()

    protected constructor(domain: Domain) {
        id = UUIDUtil.createId(domain)
    }

    @PostPersist
    @PostLoad
    protected fun load() {
        _isNew = false
    }

    override fun getId(): String = id

    override fun isNew(): Boolean = _isNew

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        val thisClass = org.hibernate.Hibernate.getClass(this)
        val otherClass = org.hibernate.Hibernate.getClass(other)

        if (thisClass != otherClass) return false

        val otherId = try {
            getIdentifier(other)
        } catch (e: Exception) {
            return false
        }
        if (this.id.isBlank() || otherId.toString().isBlank()) return false

        return this.id == otherId
    }

    override fun hashCode(): Int = id.hashCodeOrZero()

    override fun toString(): String = "${this::class.simpleName}(id=$id)"

    private fun getIdentifier(obj: Any): Serializable {
        if (obj is HibernateProxy) {
            return obj.hibernateLazyInitializer.identifier as Serializable
        }

        return (obj as? BaseEntity)?.id ?: throw SOCServerException("Invalid entity type")
    }

    private fun String?.hashCodeOrZero(): Int = this?.hashCode() ?: 0
}