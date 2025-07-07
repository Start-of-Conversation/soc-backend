package toyproject.startofconversation.notification.device.exception

import toyproject.startofconversation.common.exception.SOCNotFoundException

class DeviceNotFoundException(
    id: String, cause: Throwable? = null
) : SOCNotFoundException("Device $id is not found.", cause)