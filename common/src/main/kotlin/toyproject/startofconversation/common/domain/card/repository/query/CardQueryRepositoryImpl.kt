package toyproject.startofconversation.common.domain.card.repository.query

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import toyproject.startofconversation.common.annotation.QueryRepository
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.card.entity.QCard.card
import toyproject.startofconversation.common.domain.card.sort.CardSortField
import toyproject.startofconversation.common.domain.cardgroup.entity.QCardGroupCards.cardGroupCards
import toyproject.startofconversation.common.support.QueryDslUtil
import java.time.LocalDateTime

@QueryRepository
class CardQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CardQueryRepository {

    override fun findFilteredCards(
        cardGroupId: String?,
        from: LocalDateTime?,
        to: LocalDateTime?,
        userId: String?,
        pageable: Pageable
    ): Page<Card> {
        val whereBuilder = BooleanBuilder()

        cardGroupId?.let {
            whereBuilder.and(cardGroupCards.cardGroup.id.eq(it))
        }

        from?.let {
            whereBuilder.and(card.createdAt.goe(it))
        }

        to?.let {
            whereBuilder.and(card.createdAt.loe(it))
        }

        userId?.let {
            whereBuilder.and(card.user.id.eq(it))
        }

        val orderSpecifiers = QueryDslUtil.getOrderSpecifiers(pageable) {
            CardSortField.Companion.fromProperty(it)
        }

        val results = queryFactory
            .selectFrom(card)
            .leftJoin(card.cardGroupCards, cardGroupCards)
            .where(whereBuilder)
            .orderBy(*orderSpecifiers.toTypedArray())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(card.count())
            .from(card)
            .leftJoin(card.cardGroupCards, cardGroupCards)
            .where(whereBuilder)
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }

}