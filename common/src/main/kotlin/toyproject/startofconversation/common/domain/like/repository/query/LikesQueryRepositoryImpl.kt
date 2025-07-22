package toyproject.startofconversation.common.domain.like.repository.query

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.entity.QCardGroup.cardGroup
import toyproject.startofconversation.common.domain.cardgroup.entity.QCardGroupCards.cardGroupCards
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
        val ids = queryFactory
            .select(likes.id)
            .from(likes)
            .where(likes.user.id.eq(userId))
            .orderBy(*orderSpecifiers.toTypedArray())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val results = queryFactory
            .selectFrom(likes)
            .leftJoin(likes.cardGroup, cardGroup).fetchJoin()
            .where(likes.id.`in`(ids))
            .orderBy(*orderSpecifiers.toTypedArray())
            .fetch()

        val count = queryFactory
            .select(likes.count())
            .from(likes)
            .where(likes.user.id.eq(userId))
            .fetchOne() ?: 0L


        return PageImpl(results, pageable, count)
    }

    override fun findLikedCardGroupsByUserId(userId: String, pageable: Pageable): Page<Pair<CardGroup, Long>> {
        val ids = queryFactory
            .select(likes.cardGroup.id)
            .from(likes)
            .where(likes.user.id.eq(userId))
            .orderBy(*QueryDslUtil.getOrderSpecifiers(pageable) {
                LikeSortField.Companion.fromProperty(it)
            }.toTypedArray())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        if (ids.isEmpty()) {
            return PageImpl(emptyList(), pageable, 0)
        }

        val results = queryFactory
            .select(cardGroup, cardGroupCards.count())
            .from(cardGroup)
            .leftJoin(cardGroup.cardGroupCards, cardGroupCards)
            .where(cardGroup.id.`in`(ids))
            .groupBy(cardGroup.id)
            .fetch()

        val count = queryFactory
            .select(likes.count())
            .from(likes)
            .where(likes.user.id.eq(userId))
            .fetchOne() ?: 0L

        val orderMap = ids.withIndex().associate { it.value to it.index }
        val sortedResults = results.sortedBy { tuple ->
            orderMap[tuple.get(cardGroup)?.id] ?: Int.MAX_VALUE
        }

        val mappedResults = sortedResults.mapNotNull {
            val cg = it.get(cardGroup)
            val cnt = it.get(cardGroupCards.count())
            if (cg != null && cnt != null) cg to cnt else null
        }

        return PageImpl(mappedResults, pageable, count)
    }
}