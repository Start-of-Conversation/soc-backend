package toyproject.startofconversation.common.exception

import java.lang.RuntimeException

open class SOCException(override val message: String) : RuntimeException(message) {

}