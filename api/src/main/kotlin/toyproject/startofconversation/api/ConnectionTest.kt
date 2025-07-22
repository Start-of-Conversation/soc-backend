package toyproject.startofconversation.api

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ConnectionTest {
    val logger = LoggerFactory.getLogger(javaClass)

    fun connectDependency() = logger.info("로딩을 완료하였습니다.")

}