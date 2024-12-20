package hr.algebra.flashback

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class AbstractBaseContainerTest {
    companion object {
        @Container
        val postgresContainer = PostgreSQLContainer<Nothing>("postgres:15.4-alpine").apply {
            withDatabaseName("testdb")
            withUsername("test")
            withPassword("test")
            withReuse(true)
        }

        @JvmStatic
        @BeforeAll
        fun startContainer() {
            postgresContainer.start()
            System.setProperty("DB_URL", postgresContainer.jdbcUrl)
            System.setProperty("DB_USERNAME", postgresContainer.username)
            System.setProperty("DB_PASSWORD", postgresContainer.password)
        }


        @JvmStatic
        @AfterAll
        fun stopContainer() {
            postgresContainer.stop()
        }
    }
}