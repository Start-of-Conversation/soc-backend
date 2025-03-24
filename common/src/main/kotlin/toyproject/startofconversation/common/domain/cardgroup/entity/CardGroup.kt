package toyproject.startofconversation.common.domain.cardgroup.entity

import jakarta.persistence.*
import lombok.Builder
import lombok.Getter
import toyproject.startofconversation.common.base.BaseDateEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.user.entity.Likes
import toyproject.startofconversation.common.domain.user.entity.Users

@Getter
@Builder
@Table(name = "cardgroup")
@Entity
class CardGroup(

    @Column(nullable = false, length = 40)
    var cardGroupName: String,

    @Column(nullable = false, length = 20)
    var cardGroupSummary: String,

    @Column(nullable = false)
    var cardGroupDescription: String,

    var cardGroupThumbnail: String = "~/image/cardgroups/default_profile.png",

    @Column(nullable = false)
    var isCustomized: Boolean = false,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: Users,

    @OneToMany
    val cards: MutableSet<Card> = mutableSetOf(),

    @OneToMany
    val likes: MutableSet<Likes> = mutableSetOf()

) : BaseDateEntity(Domain.CARD_GROUP)