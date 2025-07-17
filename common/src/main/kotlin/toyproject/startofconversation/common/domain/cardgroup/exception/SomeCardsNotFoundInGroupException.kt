package toyproject.startofconversation.common.domain.cardgroup.exception

import toyproject.startofconversation.common.exception.SOCDomainViolationException

class SomeCardsNotFoundInGroupException(
    cardIds: List<String>, cause: Throwable? = null
) : SOCDomainViolationException("Some cards are not in the card group: [$cardIds]", cause)