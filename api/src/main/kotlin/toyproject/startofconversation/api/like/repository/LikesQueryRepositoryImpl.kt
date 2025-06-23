package toyproject.startofconversation.api.like.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import toyproject.startofconversation.common.domain.cardgroup.entity.QCardGroup.cardGroup
import toyproject.startofconversation.common.domain.like.entity.Likes
import toyproject.startofconversation.common.domain.like.repository.LikesQueryRepository
import toyproject.startofconversation.common.domain.like.sort.LikeSortField
import toyproject.startofconversation.common.domain.user.entity.QLikes.likes
import toyproject.startofconversation.common.support.QueryDslUtil

class LikesQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : LikesQueryRepository {


    override fun findByUserId(
        userId: String, pageable: Pageable
    ): Page<Likes> {
        val orderSpecifiers = QueryDslUtil.getOrderSpecifiers(pageable) {
            LikeSortField.fromProperty(it)
        }

        val results = queryFactory
            .selectFrom(likes)
            .leftJoin(likes.cardGroup, cardGroup).fetchJoin()
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