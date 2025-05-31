package toyproject.startofconversation.common.domain.cardgroup.exception

import toyproject.startofconversation.common.exception.SOCNotFoundException

class CardGroupNotFoundException(id: String) : SOCNotFoundException("CardGroup $id not found.")