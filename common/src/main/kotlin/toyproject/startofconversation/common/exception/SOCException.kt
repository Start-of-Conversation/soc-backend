package toyproject.startofconversation.common.exception

import java.lang.RuntimeException

class SOCException(override val message: String) : RuntimeException(message) {

}