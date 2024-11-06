package hr.algebra.flashback.model.user

import jakarta.persistence.*
import java.time.Clock
import java.time.LocalDate

@Entity
@Table(name = "flashback_user")
class User(
    @Id
    @Column(nullable = false, unique = true)
    val id: String,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var subscriptionPlan: SubscriptionPlan,
    @Column(nullable = false)
    var lastSubscriptionChange: LocalDate,
    @Column(nullable = false)
    var isProfileCompleted: Boolean,
    @Column(nullable = false)
    var dailyUpload: Int

){
    constructor() : this("", SubscriptionPlan.FREE, LocalDate.now(Clock.systemUTC()), false, 0)
}