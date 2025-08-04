package toyproject.startofconversation.common.domain.collection.exception

import toyproject.startofconversation.common.exception.SOCNotFoundException

class CollectionNotFoundException(
    id: String, cause: Throwable? = null
) : SOCNotFoundException("Collection $id not found.", cause)