package toyproject.startofconversation.notification.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.user.exception.UserNotFoundException
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.notification.controller.dto.DeviceSaveRequest
import toyproject.startofconversation.notification.domain.entity.Device
import toyproject.startofconversation.notification.domain.repository.DeviceRepository
import java.time.LocalDateTime

@Service
class DeviceService(
    private val deviceRepository: DeviceRepository,
    private val userRepository: UsersRepository
) {

    @Transactional
    fun saveDevice(request: DeviceSaveRequest, userId: String): ResponseData<Boolean> = with(request) {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException(userId)
        val device = Device(
            deviceToken = deviceToken,
            deviceId = deviceId,
            appVersion = appVersion,
            deviceType = deviceType,
            user = user,
            isPushEnabled = isPushEnabled,
            pushEnabledUpdatedAt = LocalDateTime.now()
        )
        deviceRepository.save(device)
        return ResponseData.to("Successfully saved: ", true)
    }
}