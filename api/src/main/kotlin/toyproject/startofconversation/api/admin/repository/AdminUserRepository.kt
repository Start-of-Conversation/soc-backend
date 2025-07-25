package toyproject.startofconversation.api.admin.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.Tuple
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import toyproject.startofconversation.api.admin.dto.AdminUserDto
import toyproject.startofconversation.api.admin.dto.AdminUserListResponse
import toyproject.startofconversation.auth.domain.entity.QAuth.auth
import toyproject.startofconversation.common.annotation.QueryRepository
import toyproject.startofconversation.common.domain.user.entity.QUsers.users
import toyproject.startofconversation.common.domain.user.sort.UserSortField
import toyproject.startofconversation.common.logger.logger
import toyproject.startofconversation.common.support.QueryDslUtil
import java.time.LocalDateTime

@QueryRepository
class AdminUserRepository(
    private val queryFactory: JPAQueryFactory
) {

    private val log = logger()

    fun findAllUsers(pageable: Pageable, status: Boolean? = null): Page<AdminUserListResponse> {
        val predicate = BooleanBuilder().apply {
            status?.let { and(isDeleted(it)) }
        }

        val total = queryFactory
            .select(users.id.countDistinct())
            .from(users)
            .where(predicate)
            .fetchOne() ?: 0L

        // auth까지 포함된 tuple fetch
        val flatResults = fetchTuples(pageable, predicate)

        val content = mapToResponse(flatResults)
        return PageImpl(content, pageable, total)
    }

    private fun fetchTuples(pageable: Pageable, predicate: Predicate): List<Tuple> = queryFactory
        .select(users, auth)
        .from(users)
        .leftJoin(auth).on(auth.user.id.eq(users.id))
        .where(predicate)
        .orderBy(*getOrderSpecifiers(pageable).toTypedArray())
        .offset(pageable.offset)
        .limit(pageable.pageSize.toLong())
        .fetch()

    private fun mapToResponse(flatResults: List<Tuple>): List<AdminUserListResponse> = flatResults
        .groupBy({ it[users]!! }, { it.toNullableAuthDto() })
        .map { (user, authList) ->
            AdminUserListResponse(
                id = user.id,
                nickname = user.nickname,
                registrationDate = user.createdAt,
                role = user.role,
                isWithdrawn = user.isDeleted,
                withdrawalDate = user.deletedAt,
                auth = authList.filterNotNull().toSet()
            )
        }

    private fun Tuple.toNullableAuthDto(): AdminUserDto? = this[auth]?.let {
        AdminUserDto(
            id = it.id,
            email = it.email,
            channel = it.authProvider,
            createDate = it.createdAt ?: LocalDateTime.now()
        )
    }

    private fun getOrderSpecifiers(pageable: Pageable) = QueryDslUtil.getOrderSpecifiers(pageable) {
        UserSortField.fromProperty(it)
    }

    private fun isDeleted(flag: Boolean): BooleanExpression = users.isDeleted.eq(flag)

}