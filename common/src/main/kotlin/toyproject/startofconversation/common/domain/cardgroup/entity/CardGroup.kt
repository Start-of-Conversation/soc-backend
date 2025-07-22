package toyproject.startofconversation.common.domain.cardgroup.entity

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
import toyproject.startofconversation.common.domain.like.entity.Likes
import toyproject.startofconversation.common.domain.user.entity.Users

@Table(name = "cardgroup")
@Entity
class CardGroup(

    @Column(name = "cardgroup_name", nullable = false, length = 40)
    var cardGroupName: String,

    @Column(name = "cardgroup_summary", nullable = false, length = 20)
    var cardGroupSummary: String,

    @Column(name = "cardgroup_description", nullable = false)
    var cardGroupDescription: String,

    @Column(name = "cardgroup_thumbnail", nullable = false)
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

    fun setThumbs(thumbnail: String?): CardGroup = apply {
        thumbnail?.let { cardGroupThumbnail = it }
    }

    fun setName(name: String?): CardGroup = apply {
        name?.let { cardGroupName = it }
    }

    fun setSummary(summary: String?): CardGroup = apply {
        summary?.let { cardGroupSummary = it }
    }

    fun setDesc(description: String?): CardGroup = apply {
        description?.let { cardGroupDescription = it }
    }

    fun setCustomized(customized: Boolean?): CardGroup = apply {
        customized?.let { isCustomized = it }
    }

}