package hr.algebra.flashback.service

import hr.algebra.flashback.dto.upload.PhotoMetadataDto
import hr.algebra.flashback.model.upload.Photo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Exception
import java.io.InputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.UUID

interface FileStorageService {
    fun uploadFile(fileStream: InputStream, filePath: String): Pair<String, URI>
    fun downloadFile(photo: Photo): InputStream
    fun deleteFile(photo: Photo)
}

@Service
class S3Service(
    @Autowired
    private val s3Client: S3Client,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucketName: String,
    @Value("\${cloud.aws.s3.region}")
    private val region: String,
    @Value("\${cloud.aws.s3.image.folder}")
    private val imageFolder: String
) : FileStorageService {


    override fun uploadFile(
        fileStream: InputStream,
        filePath: String,
    ): Pair<String, URI> {
        val key = "$imageFolder/$filePath"
        val fileUrl = URI.create("https://$bucketName.s3.$region.amazonaws.com/$key")

        return try {
            val request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("image/jpeg")
                .contentDisposition("inline")
                .acl("public-read")
                .build()

            val tempFile = Files.createTempFile("upload-", ".tmp")
            Files.copy(fileStream, tempFile, StandardCopyOption.REPLACE_EXISTING)

            s3Client
                .putObject(request,
                    RequestBody.fromFile(tempFile)
                )
            Pair(key, fileUrl)
        } catch (e: S3Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun downloadFile(photo: Photo): InputStream {
        try {
            val request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(photo.key)
                .build()
            val response = s3Client.getObject(request)
            val inputStream: InputStream = response
                .readAllBytes()
                .inputStream()
            return inputStream
        } catch (e: S3Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun deleteFile(photo: Photo) {
        try {
            val request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(photo.key)
                .build()
            s3Client.deleteObject(request)
        } catch (e: S3Exception) {
            e.printStackTrace()
            throw e
        }
    }

}