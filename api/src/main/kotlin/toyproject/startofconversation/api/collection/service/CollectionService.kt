package toyproject.startofconversation.api.collection.service

import org.springframework.stereotype.Service
import toyproject.startofconversation.api.collection.dto.CollectionListResponse
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.collection.repository.CollectionRepository

@Service
class CollectionService(
    private val collectionRepository: CollectionRepository
) {
    fun findCollections(userId: String): ResponseData<List<CollectionListResponse>> = ResponseData.to(
        collectionRepository.findAllByUserId(userId).map {
            CollectionListResponse(it.id, it.name, it.cards.size)
        }
    )
}