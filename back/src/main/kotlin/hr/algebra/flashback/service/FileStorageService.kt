package hr.algebra.flashback.service

import hr.algebra.flashback.config.s3.S3Handler
import hr.algebra.flashback.model.upload.Photo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Exception
import java.io.InputStream
import java.net.URI

interface FileStorageService {
    fun uploadFile(fileStream: InputStream, filePath: String): Pair<String, URI>
    fun downloadFile(photo: Photo): InputStream
    fun deleteFile(photo: Photo)
}

@Service
class S3FileStorageService(
    @Autowired
    private val s3Handler: S3Handler
) : FileStorageService {

    override fun uploadFile(
        fileStream: InputStream,
        filePath: String,
    ): Pair<String, URI> {
        val s3Client = s3Handler.s3Client
        val key = "${s3Handler.imageFolder}/$filePath"
        val fileUrl = URI.create("https://${s3Handler.bucketName}.s3.${s3Handler.bucketName}.amazonaws.com/$key")

        return try {
            val request = PutObjectRequest.builder()
                .bucket(s3Handler.bucketName)
                .key(key)
                .contentType("image/jpeg")
                .contentDisposition("inline")
                .acl("public-read")
                .build()

            s3Client.putObject(
                request,
                RequestBody.fromInputStream(
                    fileStream,
                    fileStream.available().toLong()
                )
            )
            Pair(key, fileUrl)
        } catch (e: S3Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun downloadFile(photo: Photo): InputStream {
        try {
            val s3Client = s3Handler.s3Client
            val request = GetObjectRequest.builder()
                .bucket(s3Handler.bucketName)
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
            val s3Client = s3Handler.s3Client
            val request = DeleteObjectRequest.builder()
                .bucket(s3Handler.bucketName)
                .key(photo.key)
                .build()
            s3Client.deleteObject(request)
        } catch (e: S3Exception) {
            e.printStackTrace()
            throw e
        }
    }

}