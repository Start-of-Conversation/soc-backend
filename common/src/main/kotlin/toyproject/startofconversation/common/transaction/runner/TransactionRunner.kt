package toyproject.startofconversation.common.transaction.runner

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.common.transaction.annotation.ReadOnlyTransactional

@Component
class TransactionRunner {

    @Transactional
    fun <T> run(block: () -> T): T = block()

    @ReadOnlyTransactional
    fun <T> runReadOnly(block: () -> T): T = block()
}