package toyproject.startofconversation.common.domain.collection.exception

import toyproject.startofconversation.common.exception.SOCDuplicateResourceException

class DuplicateCollectionNameException(
    name: String, cause: Throwable? = null
) : SOCDuplicateResourceException("Collection [$name] already exist.", cause)