package toyproject.startofconversation.notification.controller.dto

import toyproject.startofconversation.notification.domain.entity.value.DeviceType

data class DeviceSaveRequest(
    val deviceToken: String,
    val deviceId: String,
    val appVersion: String,
    val deviceType: DeviceType,
    val isPushEnabled: Boolean
)
