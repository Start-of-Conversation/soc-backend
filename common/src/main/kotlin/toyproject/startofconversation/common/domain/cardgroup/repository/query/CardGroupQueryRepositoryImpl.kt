package toyproject.startofconversation.common.domain.cardgroup.repository.query

import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.entity.QCardGroup.cardGroup
import toyproject.startofconversation.common.domain.cardgroup.entity.QCardGroupCards.cardGroupCards
import toyproject.startofconversation.common.domain.cardgroup.sort.CardGroupSortField
import toyproject.startofconversation.common.domain.user.entity.QUsers.users
import toyproject.startofconversation.common.support.QueryDslUtil

class CardGroupQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CardGroupQueryRepository {

    override fun findCardGroupInfoById(id: String): Pair<CardGroup, Long>? {
        val result = queryFactory
            .select(cardGroup, cardGroupCards.count())
            .from(cardGroup)
            .join(cardGroup.user, users).fetchJoin()
            .leftJoin(cardGroup.cardGroupCards, cardGroupCards)
            .where(cardGroup.id.eq(id))
            .groupBy(cardGroup.id, users.id)
            .fetchOne()

        return result?.let {
            val cg: CardGroup? = it.get(cardGroup)
            val count: Long? = it.get(cardGroupCards.count())
            if (cg != null && count != null) (cg to count) else null
        }
//        val cardGroup = queryFactory
//            .selectFrom(cardGroup)
//            .join(cardGroup.user, users).fetchJoin()
//            .where(cardGroup.id.eq(id))
//            .fetchOne()
//
//        val count = queryFactory
//            .select(cardGroupCards.count())
//            .from(cardGroupCards)
//            .where(cardGroupCards.cardGroup.id.eq(id))
//            .fetchOne() ?: 0L
//
//        return cardGroup?.let { it to count }
    }

//    override fun findCardGroupInfoAndUserById(id: String): Pair<CardGroup, Long>? {
//        val result = queryFactory
//            .select(cardGroup, cardGroupCards.count())
//            .from(cardGroup)
//            .leftJoin(cardGroup.cardGroupCards, cardGroupCards)
//            .join(cardGroup.user, users).fetchJoin()
//            .where(cardGroup.id.eq(id))
//            .groupBy(cardGroup.id)
//            .fetchOne()
//
//        return result?.let {
//            val cg: CardGroup? = it.get(cardGroup)
//            val count: Long? = it.get(cardGroupCards.count())
//            if (cg != null && count != null) (cg to count) else null
//        }
//    }

    override fun findCardGroupsWithCardCount(pageable: Pageable): Page<Pair<CardGroup, Long>> {
        val orderSpecifiers = QueryDslUtil.getOrderSpecifiers(pageable) {
            CardGroupSortField.fromProperty(it)
        }

        val results: List<Tuple> = queryFactory
            .select(cardGroup, cardGroupCards.count())
            .from(cardGroup)
            .leftJoin(cardGroup.cardGroupCards, cardGroupCards)
            .groupBy(cardGroup.id)
            .orderBy(*orderSpecifiers.toTypedArray())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val content: List<Pair<CardGroup, Long>> = results.mapNotNull {
            val group = it.get(cardGroup)
            val count = it.get(cardGroupCards.count())
            if (group != null && count != null) group to count else null
        }

        val totalCount = queryFactory
            .select(cardGroup.countDistinct())
            .from(cardGroup)
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, totalCount)
    }

    override fun findAllByUserId(userId: String, pageable: Pageable): Page<Pair<CardGroup, Long>> {
        val results: List<Tuple> = queryFactory
            .select(cardGroup, cardGroupCards.count())
            .from(cardGroup)
            .where(cardGroup.user.id.eq(userId))
            .leftJoin(cardGroup.cardGroupCards, cardGroupCards)
            .groupBy(cardGroup.id)
            .orderBy(*QueryDslUtil.getOrderSpecifiers(pageable) {
                CardGroupSortField.fromProperty(it)
            }.toTypedArray())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val content: List<Pair<CardGroup, Long>> = results.mapNotNull {
            val group = it.get(cardGroup)
            val count = it.get(cardGroupCards.count())
            if (group != null && count != null) group to count else null
        }

        val totalCount = queryFactory
            .select(cardGroup.countDistinct())
            .from(cardGroup)
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, totalCount)
    }
}