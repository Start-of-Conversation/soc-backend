package toyproject.startofconversation.common.exception

class SOCAuthException(override val message: String) : SOCException(message)  {

    companion object {
        fun of(message: String, e: Exception): SOCAuthException =
            SOCAuthException(message + " : " + e.localizedMessage)
    }
}