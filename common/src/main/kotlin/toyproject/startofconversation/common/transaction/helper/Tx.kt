package toyproject.startofconversation.common.transaction.helper

import toyproject.startofconversation.common.transaction.runner.TransactionRunner

object Tx {

    @Suppress("ktlint:standard:backing-property-naming")
    private lateinit var _txRunner: TransactionRunner

    fun initialize(txRunner: TransactionRunner) {
        _txRunner = txRunner
    }

    fun <T> writeTx(function: () -> T): T = _txRunner.run(function)

    fun <T> readTx(function: () -> T): T = _txRunner.runReadOnly(function)

}