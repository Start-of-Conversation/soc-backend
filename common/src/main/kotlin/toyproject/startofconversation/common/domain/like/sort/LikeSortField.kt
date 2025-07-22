package toyproject.startofconversation.common.domain.like.sort

import com.querydsl.core.types.dsl.ComparableExpressionBase
import toyproject.startofconversation.common.domain.like.entity.QLikes

enum class LikeSortField(
    val propertyName: String,
    val expression: ComparableExpressionBase<out Comparable<*>>
) {
    CREATED_AT("createdAt", QLikes.likes.createdAt),
    CARD_GROUP_NAME("cardGroupName", QLikes.likes.cardGroup.cardGroupName);

    companion object {
        fun fromProperty(property: String): ComparableExpressionBase<out Comparable<*>>? =
            entries.find { it.propertyName.equals(property, ignoreCase = true) }?.expression
    }
}