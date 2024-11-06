package hr.algebra.flashback.config.s3

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration

@Configuration
class S3Config {
    @Value("\${cloud.aws.s3.access-key}")
    private lateinit var accessKey: String
    @Value("\${cloud.aws.s3.secret-key}")
    private lateinit var secretKey: String
    @Value("\${cloud.aws.s3.region}")
    private lateinit var region: String

    @Bean
    fun amazonS3(): S3Client {
        val awsCredentials = AwsBasicCredentials.create(accessKey, secretKey)

        val builder = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .serviceConfiguration(S3Configuration.builder().build())

        return builder.build()
    }
}