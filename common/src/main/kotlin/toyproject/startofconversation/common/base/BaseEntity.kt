package toyproject.startofconversation.common.base

import jakarta.persistence.*
import toyproject.startofconversation.common.base.value.Domain
import java.util.*

@MappedSuperclass
open class BaseEntity(
    private val domain: Domain
) {

    @Column(name = "id", updatable = false, unique = true, nullable = false, length = 50)
    private val id: String = createId();

    @Id
    fun getId(): String = id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        return (other as BaseEntity).id == this.id
    }

    override fun hashCode(): Int = id.hashCode()

    private fun createId(): String =
        domain.toString().plus("_").plus(getRandomUUID())

    private fun getRandomUUID(): String =
        UUID.randomUUID().toString().replace("-", "");

}