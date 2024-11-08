package hr.algebra.flashback.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration

@Configuration
class ElasticConfig(
    @Value("\${spring.elasticsearch.uris}")
    private val uri: String,
    @Value("\${spring.elasticsearch.username}")
    private val username: String,
    @Value("\${spring.elasticsearch.password}")
    private val password: String,
) : ElasticsearchConfiguration() {
    override fun clientConfiguration(): ClientConfiguration {
        return ClientConfiguration.builder()
            .connectedTo(uri)
            .withBasicAuth(username, password)
            .build()
    }
}