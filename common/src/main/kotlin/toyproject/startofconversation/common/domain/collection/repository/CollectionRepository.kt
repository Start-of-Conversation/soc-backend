package toyproject.startofconversation.common.domain.collection.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.collection.entity.Collection

@Repository
interface CollectionRepository : JpaRepository<Collection, String> {
}