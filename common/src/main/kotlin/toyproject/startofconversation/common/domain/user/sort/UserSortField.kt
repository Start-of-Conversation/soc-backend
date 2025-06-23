package toyproject.startofconversation.common.domain.user.sort

import com.querydsl.core.types.dsl.ComparableExpressionBase
import toyproject.startofconversation.common.domain.user.entity.QUsers

enum class UserSortField(
    val propertyName: String,
    val expression: ComparableExpressionBase<out Comparable<*>>
) {
    CREATED_AT("createdAt", QUsers.users.createdAt),
    UPDATED_AT("updatedAt", QUsers.users.updatedAt),
    NICKNAME("nickname", QUsers.users.nickname),
    ROLE("role", QUsers.users.role);

    companion object {
        fun fromProperty(property: String): ComparableExpressionBase<out Comparable<*>>? =
            entries.find { it.propertyName.equals(property, ignoreCase = true) }?.expression
    }
}