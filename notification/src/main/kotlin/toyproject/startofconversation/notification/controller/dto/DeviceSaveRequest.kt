package toyproject.startofconversation.notification.controller.dto

import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.notification.domain.entity.Device
import toyproject.startofconversation.notification.domain.entity.value.DeviceType
import java.time.LocalDateTime

data class  DeviceSaveRequest(
    val deviceToken: String,
    val deviceId: String,
    val appVersion: String,
    val deviceType: DeviceType,
    val isPushEnabled: Boolean
) {

    fun to(user: Users) : Device = Device(
        deviceToken = deviceToken,
        deviceId = deviceId,
        appVersion = appVersion,
        deviceType = deviceType,
        user = user,
        isPushEnabled = isPushEnabled,
        pushEnabledUpdatedAt = LocalDateTime.now()
    )
}
