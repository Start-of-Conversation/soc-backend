package toyproject.startofconversation.common.base

import jakarta.persistence.*
import toyproject.startofconversation.common.base.value.Domain
import java.util.*

@MappedSuperclass
open class BaseEntity(
    private val domain: Domain
) {

    @Id
    @Column(name = "id", updatable = false, unique = true, nullable = false, length = 50)
    private val id: String = createId();

    fun getId(): String = id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseEntity

        if (domain != other.domain) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = domain.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }

    private fun createId(): String =
        domain.toString().plus("_").plus(getRandomUUID())

    private fun getRandomUUID(): String =
        UUID.randomUUID().toString().replace("-", "");

}