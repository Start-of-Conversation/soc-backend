package toyproject.startofconversation.common.domain.collection.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.collection.config.CollectionProperties
import toyproject.startofconversation.common.domain.collection.exception.DuplicateCollectionNameException
import toyproject.startofconversation.common.domain.collection.exception.MaxCollectionExceededException
import toyproject.startofconversation.common.domain.collection.repository.CollectionRepository
import toyproject.startofconversation.common.support.throwIf

@Component
class CollectionValidator(
    private val collectionRepository: CollectionRepository,
    private val collectionProperties: CollectionProperties
) {
    fun validateMaxCollectionCount(userId: String) =
        throwIf(collectionRepository.countByUserId(userId) > collectionProperties.maxCount) {
            MaxCollectionExceededException(collectionProperties.maxCount)
        }

    fun validateCollectionName(
        userId: String, name: String, normalizedName: String
    ) = throwIf(collectionRepository.existsByUserIdAndNormalizedName(userId, normalizedName)) {
        DuplicateCollectionNameException(name)
    }

}