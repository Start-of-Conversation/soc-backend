package toyproject.startofconversation.common.domain.cardgroup.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.repository.query.CardGroupQueryRepository

@Repository
interface CardGroupRepository : JpaRepository<CardGroup, String>, CardGroupQueryRepository {

    @Query("select cg from CardGroup cg join fetch cg.user u where cg.id = :id")
    fun findWithUserById(@Param("id") id: String): CardGroup?

    @EntityGraph(attributePaths = ["user"])
    fun findByIdAndUserId(id: String, userId: String): CardGroup?

}