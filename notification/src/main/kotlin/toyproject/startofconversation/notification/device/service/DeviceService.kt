package toyproject.startofconversation.notification.device.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.exception.UserNotFoundException
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.notification.device.controller.dto.AppPushInfoResponse
import toyproject.startofconversation.notification.device.controller.dto.DeviceSaveRequest
import toyproject.startofconversation.notification.device.domain.entity.Device
import toyproject.startofconversation.notification.device.domain.repository.DeviceRepository
import toyproject.startofconversation.notification.device.exception.DeviceNotFoundException
import toyproject.startofconversation.notification.device.validator.DeviceValidator

@Service
class DeviceService(
    private val deviceRepository: DeviceRepository,
    private val userRepository: UsersRepository,
    private val deviceValidator: DeviceValidator
) {

    fun getPushStatus(
        deviceId: String, userId: String
    ): ResponseData<AppPushInfoResponse> = deviceRepository.findByDeviceId(deviceId)?.let {
        deviceValidator.validateOwnership(ownerId = it.user.id, requesterId = userId)
        ResponseData.to(AppPushInfoResponse(it.isPushEnabled, it.pushEnabledUpdatedAt))
    } ?: throw DeviceNotFoundException(deviceId)

    @Transactional
    fun saveDevice(request: DeviceSaveRequest, userId: String): ResponseData<AppPushInfoResponse> {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException(userId)
        val device = getOrCreateDevice(request, user)

        deviceRepository.save(device)
        return ResponseData.to(
            message = "Successfully saved: ${device.id}",
            wrapper = AppPushInfoResponse(device.isPushEnabled, device.pushEnabledUpdatedAt)
        )
    }

    @Transactional
    fun updatePushStatus(deviceId: String, userId: String): ResponseData<AppPushInfoResponse> {
        val device = deviceRepository.findByDeviceId(deviceId)?.apply {
            deviceValidator.validateOwnership(ownerId = user.id, requesterId = userId)
            updateAppPushStatus(!this.isPushEnabled)
        } ?: throw DeviceNotFoundException(deviceId)

        return ResponseData.to(
            message = "Successfully saved: ${device.id}",
            wrapper = AppPushInfoResponse(device.isPushEnabled, device.pushEnabledUpdatedAt)
        )
    }

    private fun getOrCreateDevice(request: DeviceSaveRequest, user: Users): Device = with(request) {
        deviceRepository.findByDeviceId(deviceId)?.apply {
            deviceValidator.validateOwnership(owner = this.user, requester = user)

            updateToken(deviceToken)
            updateVersion(appVersion)
            updateAppPushStatus(isPushEnabled)
            updateLastSeenAt()
        } ?: to(user)
    }
}