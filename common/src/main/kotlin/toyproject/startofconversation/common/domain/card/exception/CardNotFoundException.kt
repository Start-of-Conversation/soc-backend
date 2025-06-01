package toyproject.startofconversation.common.domain.card.exception

import toyproject.startofconversation.common.exception.SOCNotFoundException

class CardNotFoundException(
    id: String, cause: Throwable? = null
) : SOCNotFoundException("Card $id is not found.", cause)