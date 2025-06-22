package toyproject.startofconversation.common.domain.card.exception

import toyproject.startofconversation.common.exception.SOCNotFoundException

class SomeCardsNotFoundException(
    ids: List<String>, cause: Throwable? = null
) : SOCNotFoundException("Card(s) [${ids.joinToString()}] not found.", cause)