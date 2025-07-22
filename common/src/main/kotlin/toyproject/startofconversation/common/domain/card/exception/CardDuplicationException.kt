package toyproject.startofconversation.common.domain.card.exception

import toyproject.startofconversation.common.exception.SOCDuplicateResourceException

class CardDuplicationException(
    question: String, cause: Throwable? = null
) : SOCDuplicateResourceException("Card with question '$question' already exists.", cause)