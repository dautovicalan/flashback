package hr.algebra.flashback.dto.user

import hr.algebra.flashback.model.user.SubscriptionPlan
import hr.algebra.flashback.model.user.User
import java.time.LocalDate

data class UserDto(
    val subscriptionPlan: SubscriptionPlan,
    val lastSubscriptionChange: LocalDate,
    val isProfileCompleted: Boolean,
    val dailyUpload: Int
)

fun User.toDto(): UserDto {
    return UserDto(
        subscriptionPlan = subscriptionPlan,
        lastSubscriptionChange = lastSubscriptionChange,
        isProfileCompleted = isProfileCompleted,
        dailyUpload = dailyUpload
    )
}