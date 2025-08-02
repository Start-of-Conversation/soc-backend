package toyproject.startofconversation.common.domain.collection.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.collection.entity.Collection

@Repository
interface CollectionRepository : JpaRepository<Collection, String> {

    @EntityGraph(attributePaths = ["collectionCard"])
    fun findAllByUserId(userId: String): List<Collection>

    fun countByUserId(userId: String): Long

    fun existByUserIdAndNormalizedName(userId: String, normalizedName: String): Boolean
}