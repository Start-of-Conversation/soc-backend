package toyproject.startofconversation.api

import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Slf4j
@Component
class ConnectionTest {
    val logger = LoggerFactory.getLogger(javaClass)

    fun connectDependency() = logger.info("로딩을 완료하였습니다.")

}