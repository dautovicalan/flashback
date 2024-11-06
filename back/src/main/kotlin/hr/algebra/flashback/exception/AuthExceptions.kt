package hr.algebra.flashback.exception

class UserNotFoundException(override val message: String = "User not found"): IllegalArgumentException(message)
class ProfileNotCompletedException(override val message: String): IllegalArgumentException(message)
class UserAlreadyCompletedException(override val message: String): IllegalArgumentException(message)
class NoRightsException(override val message: String): IllegalArgumentException(message)