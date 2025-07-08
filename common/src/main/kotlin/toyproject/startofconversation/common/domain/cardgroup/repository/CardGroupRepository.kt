package toyproject.startofconversation.common.domain.cardgroup.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup

@Repository
interface CardGroupRepository : JpaRepository<CardGroup, String> {

    @Query("select cg from CardGroup cg join fetch cg.user where cg.id = :id")
    fun findWithUserById(@Param("id") id: String): CardGroup?

    fun findByIdAndUserId(id: String, userId: String): CardGroup?

    fun findAllByUserId(userId: String, pageable: Pageable): Page<CardGroup>

}