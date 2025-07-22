package toyproject.startofconversation.notification.fcm.util

inline fun <T> transactionalWithNotification(
    block: () -> T,
    notify: (T) -> Unit
): T {
    val result = block()
    notify(result)
    return result
}