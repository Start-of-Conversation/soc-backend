package toyproject.startofconversation.common.support

import toyproject.startofconversation.common.exception.SOCException

/**
 * ### How to use
 *
 * ```
 * throwIf(isTrue) {
 *     IllegalStatementException("오류 발생")
 * }
 * ```
 */
fun throwIf(boolean: Boolean, exceptionSupplier: () -> SOCException) {
    if (boolean) throw exceptionSupplier()
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
fun throwIfElse(boolean: Boolean, exceptionSupplier: () -> SOCException, block: () -> Unit) {
    throwIf(boolean, exceptionSupplier)
    block()
}