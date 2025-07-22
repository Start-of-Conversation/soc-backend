package toyproject.startofconversation.common.base.value

import org.slf4j.event.Level

enum class LogLevel(val level: Level) {

    INFO(Level.INFO), WARN(Level.WARN), ERROR(Level.ERROR);

    companion object {
        fun fromSlf4jLevel(level: Level): LogLevel = when (level) {
            Level.INFO -> INFO
            Level.WARN -> WARN
            Level.ERROR -> ERROR
            else -> INFO
        }
    }
}