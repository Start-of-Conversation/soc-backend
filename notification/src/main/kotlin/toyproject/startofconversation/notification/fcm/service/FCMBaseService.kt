package toyproject.startofconversation.notification.fcm.service

import toyproject.startofconversation.notification.domain.repository.DeviceRepository

abstract class FCMBaseService(
    protected val deviceRepository: DeviceRepository
) {
    fun getDeviceToken(userId: String): List<String> = deviceRepository.findAllByUserId(userId)
        .filter { it.isPushEnabled && it.isTokenValid }
        .map { it.deviceToken }
}