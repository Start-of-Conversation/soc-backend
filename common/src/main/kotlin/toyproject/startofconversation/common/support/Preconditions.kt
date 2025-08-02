package toyproject.startofconversation.common.support

import toyproject.startofconversation.common.exception.SOCException

/**
 * ### How to use
 * Throws the given exception if `condition` is true.
 *
 * ```
 * throwIf(condition) {
 *     IllegalStateException("오류 발생")
 * }
 * ```
 */
inline fun throwIf(boolean: Boolean, exceptionSupplier: () -> SOCException) {
    if (boolean) throw exceptionSupplier()
}

/**
 * ### How to use
 * Throws the given exception if `condition` is false.
 *
 * ```
 * throwIf(condition) {
 *     IllegalStateException("오류 발생")
 * }
 * ```
 */
inline fun throwIfNot(boolean: Boolean, exceptionSupplier: () -> SOCException) {
    throwIf(!boolean, exceptionSupplier)
}

/**
 * ### How to use
 *
 * ```
 * throwIfElse(isDuplicate, { DuplicateNameException(name) }) {
 *     println("저장 로직 실행")
 * }
 * ```
 */
inline fun throwIfElse(boolean: Boolean, exceptionSupplier: () -> SOCException, block: () -> Unit) {
    throwIf(boolean, exceptionSupplier)
    block()
}
