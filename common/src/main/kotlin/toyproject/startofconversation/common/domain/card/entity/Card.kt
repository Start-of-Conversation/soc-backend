package toyproject.startofconversation.common.domain.card.entity

import jakarta.persistence.*
import toyproject.startofconversation.common.base.BaseDateEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroupCards
import toyproject.startofconversation.common.domain.user.entity.Users

@Entity
@Table(name = "card")
class Card(

    @Column(nullable = false)
    var question: String,

    @OneToMany(mappedBy = "card", cascade = [CascadeType.ALL], orphanRemoval = true)
    val cardGroupCards: MutableSet<CardGroupCards> = mutableSetOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: Users,

    @Column(nullable = false)
    val normalizedQuestion: String = normalize(question)

) : BaseDateEntity(Domain.CARD) {

    fun addCardGroup(cardGroupCards: CardGroupCards) = this.cardGroupCards.add(cardGroupCards)

    fun updateQuestion(question: String) {
        this.question = question
    }

    companion object {
        fun from(question: String, cardGroup: CardGroup, user: Users): Card {
            val card = Card(question = question, user = user)
            val cardGroupCards = CardGroupCards(cardGroup = cardGroup, card = card)
            card.addCardGroup(cardGroupCards)
            return card
        }

        fun normalize(input: String): String =
            input.trim().replace(Regex("\\s+"), " ").lowercase()
    }
}