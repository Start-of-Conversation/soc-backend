package toyproject.startofconversation.common.support

import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.ComparableExpressionBase
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

object QueryDslUtil {

    /**
     * 동적으로 필드 이름(fieldName)을 받아서 정렬
     *
     * `val expr = QUser.user.createdAt`
     */
    fun getSortedColumn(order: Order, parent: Path<*>, fieldName: String): OrderSpecifier<*> {
        val pathBuilder = PathBuilder(parent.type, parent.metadata)
        val fieldPath = pathBuilder.getComparable(fieldName, Comparable::class.java)
        return OrderSpecifier(order, fieldPath)
    }

    /**
     * 이미 타입이 정해진 ComparableExpressionBase<T>를 기반으로 정렬
     * 각 도메인 유틸에서 fieldMap으로 가져온 Expression을 정렬할 때 사용
     *
     * `val orderSpecifier = QueryDslUtil.orderBy(Sort.Direction.DESC, expr)`
     */
    fun <T : Comparable<T>> orderBy(
        direction: Sort.Direction,
        expression: ComparableExpressionBase<T>
    ): OrderSpecifier<T> = OrderSpecifier(
        if (direction.isAscending) Order.ASC else Order.DESC, expression
    )

    /**
     * Pageable에 들어있는 정렬 정보(Sort)를 기반으로 다수의 정렬 조건을 한 번에 처리
     * 동적 정렬 조건 처리
     */
    fun getOrderSpecifiers(
        pageable: Pageable, fieldMapper: (String) -> ComparableExpressionBase<out Comparable<*>>?
    ): List<OrderSpecifier<*>> = pageable.sort.mapNotNull { sort ->
        val direction = if (sort.isAscending) Order.ASC else Order.DESC
        val expression = fieldMapper(sort.property) ?: return@mapNotNull null
        @Suppress("UNCHECKED_CAST")
        OrderSpecifier(direction, expression as Expression<Comparable<Any>>)
    }

}