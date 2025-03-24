package toyproject.startofconversation.common.domain.user.entity

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.Builder
import lombok.Getter
import toyproject.startofconversation.common.base.BaseCreatedEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup

@Entity
@Getter
@Builder
@Table(name = "likes")
class Likes(

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: Users,

    @ManyToOne
    @JoinColumn(name = "cardgroup_id")
    val cardGroup: CardGroup

) : BaseCreatedEntity(Domain.Likes)