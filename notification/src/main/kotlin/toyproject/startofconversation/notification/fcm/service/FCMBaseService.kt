package toyproject.startofconversation.notification.fcm.service

import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.notification.device.domain.entity.Device
import toyproject.startofconversation.notification.device.domain.repository.DeviceRepository

abstract class FCMBaseService(
    protected val deviceRepository: DeviceRepository
) {
    fun getDeviceToken(userId: String): List<String> = getToken(deviceRepository.findAllByUserId(userId))

    fun getDeviceToken(user: Users): List<String> = getToken(deviceRepository.findAllByUser(user))

    private fun getToken(devices: List<Device>): List<String> = devices
        .filter { it.isPushEnabled && it.isTokenValid }
        .map { it.deviceToken }
}