package hr.algebra.flashback.dto.user

import hr.algebra.flashback.model.user.SubscriptionPlan

data class CompleteProfileDto(
    val subscriptionPlan: SubscriptionPlan
)
