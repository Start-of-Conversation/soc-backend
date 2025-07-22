package toyproject.startofconversation.common.domain.card.sort

import com.querydsl.core.types.dsl.ComparableExpressionBase
import toyproject.startofconversation.common.domain.card.entity.QCard

enum class CardSortField(
    val propertyName: String,
    val expression: ComparableExpressionBase<out Comparable<*>>
) {
    CREATED_AT("createdAt", QCard.card.createdAt),
    UPDATED_AT("updatedAt", QCard.card.updatedAt);

    companion object {
        fun fromProperty(property: String): ComparableExpressionBase<out Comparable<*>>? =
            CardSortField.entries.find { it.propertyName.equals(property, ignoreCase = true) }?.expression
    }
}