package toyproject.startofconversation.common.domain.card.entity

import jakarta.persistence.*
import lombok.Builder
import lombok.Getter
import toyproject.startofconversation.common.base.BaseDateEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup

@Entity
@Getter
@Table(name = "card")
@Builder
class Card(

    @Column(nullable = false)
    var question: String,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "cardgroup_id")
    var cardGroup: CardGroup

) : BaseDateEntity(Domain.CARD)