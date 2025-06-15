package toyproject.startofconversation.common.base

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import toyproject.startofconversation.common.base.value.Domain
import java.io.Serializable
import java.util.*

@MappedSuperclass
open class BaseEntity : Serializable {

    @Id
    @Column(name = "id", updatable = false, unique = true, nullable = false, length = 50)
    lateinit var id: String
        protected set

    protected constructor()

    protected constructor(domain: Domain) {
        id = createId(domain)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        return (other as BaseEntity).id == this.id
    }

    override fun hashCode(): Int = id.hashCode()

    private fun createId(domain: Domain): String =
        domain.toString().plus("_").plus(getRandomUUID())

    private fun getRandomUUID(): String =
        UUID.randomUUID().toString().replace("-", "");

}