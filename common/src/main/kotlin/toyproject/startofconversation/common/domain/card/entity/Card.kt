package toyproject.startofconversation.common.domain.card.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import toyproject.startofconversation.common.base.BaseDateEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroupCards
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.support.normalize

@Entity
@Table(name = "card")
class Card(

    @Column(nullable = false)
    var question: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: Users,

    @Column(nullable = false)
    var normalizedQuestion: String = normalize(question)

) : BaseDateEntity(Domain.CARD) {

    @OneToMany(mappedBy = "card", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val cardGroupCards: MutableSet<CardGroupCards> = mutableSetOf()

    fun updateQuestion(question: String, normalizedQuestion: String = normalize(question)): Card {
        this.question = question
        this.normalizedQuestion = normalizedQuestion
        return this
    }

    companion object {
        fun from(
            question: String,
            user: Users,
            normalizedQuestion: String = normalize(question)
        ): Card = Card(
            question = question,
            user = user,
            normalizedQuestion = normalizedQuestion
        )
    }
}