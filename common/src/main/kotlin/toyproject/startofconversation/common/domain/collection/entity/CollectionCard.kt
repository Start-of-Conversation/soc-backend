package toyproject.startofconversation.common.domain.collection.entity

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import toyproject.startofconversation.common.domain.card.entity.Card
import java.time.LocalDateTime

@Entity
@Table(name = "collection_cards")
@EntityListeners(AuditingEntityListener::class)
class CollectionCard(

    @EmbeddedId
    val id: CollectionCardId,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("collectionId")
    @JoinColumn(name = "collection_id", nullable = false)
    val collection: Collection,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cardId")
    @JoinColumn(name = "card_id", nullable = false)
    val card: Card,

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime? = null

) {

}