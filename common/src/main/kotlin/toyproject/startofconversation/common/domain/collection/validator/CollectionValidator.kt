package toyproject.startofconversation.common.domain.collection.validator

import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.collection.config.CollectionProperties
import toyproject.startofconversation.common.domain.collection.exception.DuplicateCollectionNameException
import toyproject.startofconversation.common.domain.collection.exception.MaxCollectionExceededException
import toyproject.startofconversation.common.domain.collection.repository.CollectionRepository

@Component
class CollectionValidator(
    private val collectionRepository: CollectionRepository,
    private val collectionProperties: CollectionProperties
) {
    fun validateMaxCollectionCount(userId: String) =
        require(collectionRepository.countByUserId(userId) > collectionProperties.maxCount) {
            throw MaxCollectionExceededException(collectionProperties.maxCount)
        }

    fun validateCollectionName(
        userId: String, name: String, normalizedName: String
    ) = require(collectionRepository.existByUserIdAndNormalizedName(userId, normalizedName)) {
        throw DuplicateCollectionNameException(name)
    }

}