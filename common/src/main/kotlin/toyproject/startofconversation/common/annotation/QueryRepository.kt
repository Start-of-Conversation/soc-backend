package toyproject.startofconversation.common.annotation

import org.springframework.stereotype.Repository

/**
 * 커스텀 Repository입니다
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repository
annotation class QueryRepository
