package toyproject.startofconversation.common.domain.like.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.like.entity.Likes
import toyproject.startofconversation.common.domain.user.entity.Users

@Repository
interface LikesRepository : JpaRepository<Likes, String>, LikesQueryRepository {

    fun existsByUserAndCardGroup(user: Users, cardGroup: CardGroup): Boolean

    fun existsByUserIdAndCardGroupId(userId: String, cardGroupId: String): Boolean

    fun deleteByUserIdAndCardGroupId(userId: String, cardGroupId: String)

}