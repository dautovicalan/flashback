package hr.algebra.flashback.repository

import hr.algebra.flashback.model.user.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, String>