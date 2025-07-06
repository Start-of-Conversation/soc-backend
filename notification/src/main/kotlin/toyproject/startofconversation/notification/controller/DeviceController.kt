package toyproject.startofconversation.notification.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.common.base.controller.BaseController
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.notification.controller.dto.DeviceSaveRequest
import toyproject.startofconversation.notification.service.DeviceService

@RestController
@RequestMapping("/api/devices")
class DeviceController(
    private val deviceService: DeviceService
) : BaseController() {

    @PostMapping
    fun registerDevice(@RequestBody request: DeviceSaveRequest): ResponseData<Boolean> =
        deviceService.saveDevice(request, getUserId())

}