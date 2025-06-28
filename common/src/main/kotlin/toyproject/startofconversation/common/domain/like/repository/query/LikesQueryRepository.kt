package toyproject.startofconversation.common.domain.like.repository.query

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import toyproject.startofconversation.common.domain.like.entity.Likes

interface LikesQueryRepository {

    fun findByUserId(userId: String, pageable: Pageable): Page<Likes>

}