package toyproject.startofconversation.log.system.dto

import toyproject.startofconversation.common.base.value.LogLevel
import toyproject.startofconversation.common.base.value.ModuleType
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.log.system.domain.entity.SystemLog

data class SystemLogEvent(
    val userId: String?,
    val ipAddress: String,
    val module: ModuleType,
    val action: String,
    val logLevel: LogLevel,
    val errorMessage: String?
) {
    fun to(block: () -> Users?): SystemLog = SystemLog(
        user = block(),
        ipAddress = ipAddress,
        module = module,
        action = action,
        logLevel = logLevel,
        errorMessage = errorMessage
    )

    companion object {
        fun info(
            userId: String?,
            ipAddress: String,
            module: ModuleType,
            action: String
        ): SystemLogEvent = SystemLogEvent(
            userId = userId,
            ipAddress = ipAddress,
            module = module,
            action = action,
            errorMessage = null,
            logLevel = LogLevel.INFO
        )

        fun error(
            userId: String?,
            ipAddress: String,
            module: ModuleType,
            action: String,
            errorMessage: String?
        ): SystemLogEvent = SystemLogEvent(
            userId = userId,
            ipAddress = ipAddress,
            module = module,
            action = action,
            errorMessage = errorMessage,
            logLevel = LogLevel.ERROR
        )
    }
}