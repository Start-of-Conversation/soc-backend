package toyproject.startofconversation.common.domain.like.repository.query

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import toyproject.startofconversation.common.domain.cardgroup.entity.QCardGroup
import toyproject.startofconversation.common.domain.like.entity.Likes
import toyproject.startofconversation.common.domain.like.entity.QLikes.likes
import toyproject.startofconversation.common.domain.like.sort.LikeSortField
import toyproject.startofconversation.common.support.QueryDslUtil

class LikesQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : LikesQueryRepository {


    override fun findByUserId(
        userId: String, pageable: Pageable
    ): Page<Likes> {
        val orderSpecifiers = QueryDslUtil.getOrderSpecifiers(pageable) {
            LikeSortField.Companion.fromProperty(it)
        }

        val results = queryFactory
            .selectFrom(likes)
            .leftJoin(likes.cardGroup, QCardGroup.cardGroup).fetchJoin()
            .where(likes.user.id.eq(userId))
            .orderBy(*orderSpecifiers.toTypedArray())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory
            .select(likes.count())
            .from(likes)
            .where(likes.user.id.eq(userId))
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, count)
    }
}