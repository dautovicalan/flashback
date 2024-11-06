package hr.algebra.flashback.dto.user

import hr.algebra.flashback.model.user.SubscriptionPlan
import jakarta.validation.constraints.Null

data class UpdateUserDataDto(
    @Null
    val firstName: String?,
    @Null
    val lastName: String?,
    val subscriptionPlan: SubscriptionPlan,
    val resetDailyUploads: Boolean
)
