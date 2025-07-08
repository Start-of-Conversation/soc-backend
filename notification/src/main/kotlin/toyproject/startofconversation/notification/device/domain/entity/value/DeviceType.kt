package toyproject.startofconversation.notification.device.domain.entity.value

import com.fasterxml.jackson.annotation.JsonCreator

enum class DeviceType {
    IOS, IPAD, WEB;

    companion object {
        @JvmStatic
        @JsonCreator
        fun from(value: String): DeviceType {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown device type: $value")
        }
    }
}