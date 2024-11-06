package hr.algebra.flashback.service

import org.springframework.beans.factory.annotation.Autowired


class AuthService(
    @Autowired
    private val userService: UserService,
)