package toyproject.startofconversation.common.domain.card.exception

import toyproject.startofconversation.common.exception.SOCNotFoundException

class CardNotFoundException(id: String) : SOCNotFoundException("Card $id is not found.")