package toyproject.startofconversation.api.card.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import toyproject.startofconversation.common.annotation.QueryRepository
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.card.entity.QCard.card
import toyproject.startofconversation.common.domain.card.repository.CardQueryRepository
import toyproject.startofconversation.common.domain.cardgroup.entity.QCardGroupCards.cardGroupCards
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

        val results = queryFactory
            .selectFrom(card)
            .leftJoin(cardGroupCards).on(cardGroupCards.card.eq(card))
            .where(whereBuilder)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(card.createdAt.desc())
            .fetch()

        val total = queryFactory
            .select(card.count())
            .from(card)
            .leftJoin(cardGroupCards).on(cardGroupCards.card.eq(card))
            .where(whereBuilder)
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }

}