package toyproject.startofconversation.notification.device.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.common.base.controller.BaseController
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.notification.device.controller.dto.AppPushInfoResponse
import toyproject.startofconversation.notification.device.controller.dto.DeviceSaveRequest
import toyproject.startofconversation.notification.device.service.DeviceService

@RestController
@RequestMapping("/api/devices")
class DeviceController(
    private val deviceService: DeviceService
) : BaseController() {

    @GetMapping("/{deviceId}")
    fun getPushStatus(@PathVariable deviceId: String): ResponseData<AppPushInfoResponse> =
        deviceService.getPushStatus(deviceId, getUserId())

    @PostMapping
    fun registerDevice(@RequestBody request: DeviceSaveRequest): ResponseData<AppPushInfoResponse> =
        deviceService.saveDevice(request, getUserId())

    @PutMapping("/{deviceId}")
    fun changePushStatus(@PathVariable deviceId: String): ResponseData<AppPushInfoResponse> =
        deviceService.updatePushStatus(deviceId, getUserId())
}