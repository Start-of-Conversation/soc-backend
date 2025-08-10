package toyproject.startofconversation.common.transaction.config

import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import toyproject.startofconversation.common.transaction.helper.Tx
import toyproject.startofconversation.common.transaction.runner.TransactionRunner

@Configuration
class TransactionConfig {

    @Bean("txInitBean")
    fun txInitialize(txRunner: TransactionRunner): InitializingBean =
        InitializingBean { Tx.initialize(txRunner) }
}