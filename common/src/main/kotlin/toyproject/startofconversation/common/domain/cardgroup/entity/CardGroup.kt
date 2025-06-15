package toyproject.startofconversation.common.domain.cardgroup.entity

import jakarta.persistence.*
import toyproject.startofconversation.common.base.BaseDateEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.user.entity.Likes
import toyproject.startofconversation.common.domain.user.entity.Users

@Table(name = "cardgroup")
@Entity
class CardGroup(

    @Column(nullable = false, length = 40)
    var cardGroupName: String,

    @Column(nullable = false, length = 20)
    var cardGroupSummary: String,

    @Column(nullable = false)
    var cardGroupDescription: String,

    @Column(nullable = false)
    var cardGroupThumbnail: String = "~/image/cardgroups/default_profile.png",

    @Column(nullable = false)
    var isCustomized: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: Users

) : BaseDateEntity(Domain.CARD_GROUP) {

    @OneToMany(mappedBy = "cardGroup", cascade = [CascadeType.ALL], orphanRemoval = true)
    val cardGroupCards: MutableSet<CardGroupCards> = mutableSetOf()

    @OneToMany(mappedBy = "cardGroup")
    val likes: MutableSet<Likes> = mutableSetOf()

    fun setThumbs(thumbnail: String?): CardGroup = thumbnail?.let {
        cardGroupThumbnail = thumbnail
        this
    } ?: this

}