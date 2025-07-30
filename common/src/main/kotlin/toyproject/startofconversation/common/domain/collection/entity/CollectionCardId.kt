package toyproject.startofconversation.common.domain.collection.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
class CollectionCardId(
    @Column(name = "collection_id")
    val collectionId: String,

    @Column(name = "card_id")
    val cardId: String
) : Serializable