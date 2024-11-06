package hr.algebra.flashback.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

interface StorageUrlService {
    fun getPhotoUrl(key: String): String
}

@Service
class S3StorageUrlService(
    @Value("\${cloud.aws.s3.bucket}")
    private val s3BucketName: String,
    @Value("\${cloud.aws.s3.region}")
    private val s3Region: String
): StorageUrlService {
    override fun getPhotoUrl(key: String): String {
        return "https://$s3BucketName.s3.$s3Region.amazonaws.com/$key"
    }
}