package toyproject.startofconversation.common.domain.card.entity

import jakarta.persistence.*
import toyproject.startofconversation.common.base.BaseDateEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.user.entity.Users

@Entity
@Table(name = "card")
class Card(

    @Column(nullable = false)
    var question: String,

    @ManyToMany(mappedBy = "cards")
    var cardGroups: MutableSet<CardGroup> = mutableSetOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: Users

) : BaseDateEntity(Domain.CARD) {

    fun addCardGroup(cardGroup: CardGroup) = this.cardGroups.add(cardGroup)

    fun updateQuestion(question: String) {
        this.question = question
    }

    companion object {
        fun from(question: String, cardGroup: CardGroup, user: Users): Card {
            val card = Card(question = question, user = user)
            card.addCardGroup(cardGroup)
            return card
        }
    }
}