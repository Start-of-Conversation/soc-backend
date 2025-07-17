package toyproject.startofconversation.common.domain.cardgroup.repository.query

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup

interface CardGroupQueryRepository {

    fun findCardGroupInfoById(id: String): Pair<CardGroup, Long>?

//    fun findCardGroupInfoAndUserById(id: String): Pair<CardGroup, Long>?

    fun findCardGroupsWithCardCount(pageable: Pageable): Page<Pair<CardGroup, Long>>

    fun findAllByUserId(userId: String, pageable: Pageable): Page<Pair<CardGroup, Long>>
}