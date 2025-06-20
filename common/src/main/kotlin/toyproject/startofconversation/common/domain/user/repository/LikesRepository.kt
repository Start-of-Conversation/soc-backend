package toyproject.startofconversation.common.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.user.entity.Likes
import toyproject.startofconversation.common.domain.user.entity.Users

@Repository
interface LikesRepository : JpaRepository<Likes, String> {

    fun existsByUserAndCardGroup(user: Users, cardGroup: CardGroup): Boolean

}