package toyproject.startofconversation.common.domain.cardgroup.exception

import toyproject.startofconversation.common.exception.SOCNotFoundException

class CardGroupNotFoundException(
    id: String, cause: Throwable?
) : SOCNotFoundException("CardGroup $id not found.", cause)