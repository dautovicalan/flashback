package hr.algebra.flashback.model.user

enum class SubscriptionPlan(val price: Double, val uploadLimit: Int, val description: String) {
    FREE(0.0, 1, "Free subscription plan", ),
    PRO(9.99, 10, "Basic subscription plan"),
    GOLD(19.99, 20, "Premium subscription plan"),
}