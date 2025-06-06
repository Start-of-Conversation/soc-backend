package toyproject.startofconversation.common.domain.card.exception

import toyproject.startofconversation.common.exception.SOCDuplicateResourceException

class CardDuplicationException(
    cardId: String, question: String, cause: Throwable? = null
) : SOCDuplicateResourceException("Card '$cardId' with question '$question' already exists.", cause)