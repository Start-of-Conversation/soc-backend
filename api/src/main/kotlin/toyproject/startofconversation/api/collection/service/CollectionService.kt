package toyproject.startofconversation.api.collection.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.collection.dto.CollectionCreateRequest
import toyproject.startofconversation.api.collection.dto.CollectionListResponse
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.collection.entity.Collection
import toyproject.startofconversation.common.domain.collection.repository.CollectionRepository
import toyproject.startofconversation.common.domain.collection.validator.CollectionValidator
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.support.normalize

@Service
@LoginUserAccess
class CollectionService(
    private val collectionRepository: CollectionRepository,
    private val collectionTransactionalService: CollectionTransactionalService
) {
    fun findCollections(userId: String): ResponseData<List<CollectionListResponse>> = ResponseData.to(
        collectionRepository.findAllByUserId(userId).map {
            CollectionListResponse(it.id, it.name, it.cards.size)
        }
    )

    fun saveAndFindCollections(
        userId: String, request: CollectionCreateRequest
    ): ResponseData<List<CollectionListResponse>> {
        collectionTransactionalService.saveCollection(userId, request)
        return findCollections(userId)
    }
}

@Service
class CollectionTransactionalService(
    private val collectionRepository: CollectionRepository,
    private val userRepository: UsersRepository,
    private val collectionValidator: CollectionValidator
) {
    @Transactional
    fun saveCollection(
        userId: String, request: CollectionCreateRequest
    ) {
        collectionValidator.validateMaxCollectionCount(userId)
        val normalizedName = normalize(request.name)
        collectionValidator.validateCollectionName(userId, request.name, normalizedName)

        val user = userRepository.getReferenceById(userId)

        val collection = Collection(name = request.name, user = user, normalizedName = normalizedName)
        collectionRepository.save(collection)
    }
}