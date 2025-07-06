package toyproject.startofconversation.notification.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.exception.UserNotFoundException
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.notification.fcm.config.properties.FCMProperties
import toyproject.startofconversation.notification.controller.dto.DeviceSaveRequest
import toyproject.startofconversation.notification.domain.entity.Device
import toyproject.startofconversation.notification.domain.repository.DeviceRepository

@Service
class DeviceService(
    private val deviceRepository: DeviceRepository,
    private val userRepository: UsersRepository
) {

    @Transactional
    fun saveDevice(request: DeviceSaveRequest, userId: String): ResponseData<Boolean> {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException(userId)
        val device = getOrCreateDevice(request, user)

        deviceRepository.save(device)
        return ResponseData.to("Successfully saved: ${device.id}", true)
    }

    private fun getOrCreateDevice(request: DeviceSaveRequest, user: Users): Device = with(request) {
        deviceRepository.findByDeviceId(deviceId)?.apply {
            updateToken(deviceToken)
            updateVersion(appVersion)
            updateAppPushStatus(isPushEnabled)
            updateLastSeenAt()
        } ?: to(user)
    }
}