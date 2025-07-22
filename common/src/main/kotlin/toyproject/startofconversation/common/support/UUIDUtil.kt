package toyproject.startofconversation.common.support

import toyproject.startofconversation.common.base.value.Domain
import java.util.UUID

object UUIDUtil {

    fun createId(domain: Domain): String = createId(domain.toString())

    fun createId(domain: String): String = domain.plus("_").plus(getRandomUUID())

    fun getRandomUUID(): String =
        UUID.randomUUID().toString().replace("-", "")

}