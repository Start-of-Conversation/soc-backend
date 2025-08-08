package toyproject.startofconversation.common.domain.collection.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import toyproject.startofconversation.common.base.BaseCreatedEntity
import toyproject.startofconversation.common.base.value.Domain
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.support.normalize

@Entity
@Table(name = "collection")
class Collection(

    @Column(name = "collection_name", nullable = false, length = 50)
    var name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: Users,

    @Column(name = "normalized_name", nullable = false)
    var normalizedName: String = normalize(name)

) : BaseCreatedEntity(Domain.COLLECTION) {

    @OneToMany(mappedBy = "collection")
    var cards: MutableSet<CollectionCard> = mutableSetOf()

    fun updateName(newName: String) : Collection {
        this.name = newName
        return this
    }

}