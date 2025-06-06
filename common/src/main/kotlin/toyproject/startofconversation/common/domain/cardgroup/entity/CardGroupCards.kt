package toyproject.startofconversation.common.domain.cardgroup.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import toyproject.startofconversation.common.base.BaseCreatedEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.card.entity.Card

@Table(name = "cardgroup_cards")
@Entity
class CardGroupCards(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardgroup_id")
    val cardGroup: CardGroup,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    val card: Card

) : BaseCreatedEntity(Domain.CARDGROUP_CARDS) {
}