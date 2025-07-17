package toyproject.startofconversation.common.domain.cardgroup.sort

import com.querydsl.core.types.dsl.ComparableExpressionBase
import toyproject.startofconversation.common.domain.cardgroup.entity.QCardGroup

enum class CardGroupSortField(
    val propertyName: String,
    val expression: ComparableExpressionBase<out Comparable<*>>
) {
    CREATED_AT("createdAt", QCardGroup.cardGroup.createdAt),
    UPDATED_AT("updatedAt", QCardGroup.cardGroup.updatedAt);

    companion object {
        fun fromProperty(property: String): ComparableExpressionBase<out Comparable<*>>? =
            CardGroupSortField.entries.find { it.propertyName.equals(property, ignoreCase = true) }?.expression
    }
}