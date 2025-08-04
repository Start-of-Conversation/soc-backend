package toyproject.startofconversation.api.collection.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.annotation.LoginUserAccess
import toyproject.startofconversation.api.collection.dto.CollectionCreateRequest
import toyproject.startofconversation.api.collection.dto.CollectionListResponse
import toyproject.startofconversation.api.collection.dto.CollectionUpdateRequest
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.collection.entity.Collection
import toyproject.startofconversation.common.domain.collection.exception.CollectionNotFoundException
import toyproject.startofconversation.common.domain.collection.repository.CollectionRepository
import toyproject.startofconversation.common.domain.collection.validator.CollectionValidator
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.lock.strategy.LockService
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

    fun updateCollection(
        userId: String, collectionId: String, request: CollectionUpdateRequest
    ): ResponseData<List<CollectionListResponse>> {
        collectionTransactionalService.updateCollection(collectionId, request)
        return findCollections(userId)
    }
}

@Service
class CollectionTransactionalService(
    private val collectionRepository: CollectionRepository,
    private val userRepository: UsersRepository,
    private val lockService: LockService,
    private val collectionValidator: CollectionValidator
) {
    @Transactional
    fun saveCollection(
        userId: String, request: CollectionCreateRequest
    ) = withCollectionLock(userId) {
        collectionValidator.validateMaxCollectionCount(userId)
        val normalizedName = normalize(request.name)
        collectionValidator.validateCollectionName(userId, request.name, normalizedName)

        val user = userRepository.getReferenceById(userId)

        val collection = Collection(name = request.name, user = user, normalizedName = normalizedName)
        collectionRepository.save(collection)
    }

    @Transactional
    fun updateCollection(
        collectionId: String, request: CollectionUpdateRequest
    ) = collectionRepository.findByIdOrNull(collectionId)
        ?.updateName(request.newName)
        ?: throw CollectionNotFoundException(collectionId)

    private fun <T> withCollectionLock(userId: String, block: () -> T): T =
        lockService.executeWithLock(lockKey = collectionLockKey(userId), block = block)

    companion object {
        private fun collectionLockKey(userId: String) = "lock:collection:$userId:add-collection"
    }
}