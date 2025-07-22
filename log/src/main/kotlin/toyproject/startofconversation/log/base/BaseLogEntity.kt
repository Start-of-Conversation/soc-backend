package toyproject.startofconversation.log.base

import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import toyproject.startofconversation.common.base.BaseCreatedEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.base.value.LogLevel

abstract class BaseLogEntity(
    domain: Domain,

    @Column(name = "log_level")
    @Enumerated(EnumType.STRING)
    var logLevel: LogLevel = LogLevel.INFO,

    @Column(name = "error_message")
    var errorMessage: String? = null

) : BaseCreatedEntity(domain) {

}