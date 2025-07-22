package toyproject.startofconversation.common.domain.like.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.like.entity.Likes
import toyproject.startofconversation.common.domain.like.repository.query.LikesQueryRepository

@Repository
interface LikesRepository : JpaRepository<Likes, String>, LikesQueryRepository {

    @Query("select count(l) > 0 from Likes l where l.user.id = :userId and l.cardGroup.id = :cardGroupId")
    fun existsByUserIdAndCardGroupId(userId: String, cardGroupId: String): Boolean

    fun deleteByUserIdAndCardGroupId(userId: String, cardGroupId: String)

}