package toyproject.startofconversation.common.domain.collection

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import toyproject.startofconversation.common.base.BaseCreatedEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.user.entity.Users

@Entity
@Table(name = "collection")
class Collection(

    @Column(name = "collection_name", nullable = false, length = 50)
    var name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: Users

    ) : BaseCreatedEntity(Domain.COLLECTION) {
}