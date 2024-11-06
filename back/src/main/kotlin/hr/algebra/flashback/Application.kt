package hr.algebra.flashback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableJpaRepositories("hr.algebra.flashback.repository")
@EnableElasticsearchRepositories("hr.algebra.flashback.repository")
@EnableScheduling
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
