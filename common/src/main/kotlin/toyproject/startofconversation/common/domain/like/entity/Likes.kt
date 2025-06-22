package toyproject.startofconversation.common.domain.like.entity

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import toyproject.startofconversation.common.base.BaseCreatedEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.user.entity.Users

@Entity
@Table(name = "likes")
class Likes(

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: Users,

    @ManyToOne
    @JoinColumn(name = "cardgroup_id", referencedColumnName = "id")
    val cardGroup: CardGroup

) : BaseCreatedEntity(Domain.LIKES)