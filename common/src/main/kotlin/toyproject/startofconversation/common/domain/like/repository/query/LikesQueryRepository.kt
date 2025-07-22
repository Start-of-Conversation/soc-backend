package toyproject.startofconversation.common.domain.like.repository.query

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.like.entity.Likes

interface LikesQueryRepository {

    fun findByUserId(userId: String, pageable: Pageable): Page<Likes>

    fun findLikedCardGroupsByUserId(userId: String, pageable: Pageable): Page<Pair<CardGroup, Long>>

}