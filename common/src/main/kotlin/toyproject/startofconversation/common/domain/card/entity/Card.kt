package toyproject.startofconversation.common.domain.card.entity

import jakarta.persistence.*
import toyproject.startofconversation.common.base.BaseDateEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup

@Entity
@Table(name = "card")
class Card(

    @Column(nullable = false)
    var question: String,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "cardgroup_id", referencedColumnName = "id")
    var cardGroup: CardGroup

) : BaseDateEntity(Domain.CARD)