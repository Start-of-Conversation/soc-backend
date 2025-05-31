package toyproject.startofconversation.common.domain.cardgroup.exception

import toyproject.startofconversation.common.exception.SOCNotFoundException

class MaxGroupCapacityExceededException(maxSize: Int) :
    SOCNotFoundException("Group can only have up to $maxSize members.")