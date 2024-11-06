package hr.algebra.flashback.service

import hr.algebra.flashback.dto.upload.PhotoFormat
import hr.algebra.flashback.dto.upload.PhotoMetadataDto
import hr.algebra.flashback.dto.upload.UploadResultDto
import hr.algebra.flashback.exception.DailyUploadReachedException
import hr.algebra.flashback.exception.ProfileNotCompletedException
import hr.algebra.flashback.exception.WrongFileFormatException
import hr.algebra.flashback.model.upload.Photo
import hr.algebra.flashback.model.upload.PhotoTag
import hr.algebra.flashback.repository.PhotoRepository
import hr.algebra.flashback.util.getPhotoType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.InputStream
import java.net.URI
import java.util.*
import javax.imageio.ImageIO

@Service
class UploadService(
    @Autowired
    private val photoRepository: PhotoRepository,
    @Autowired
    private val fileStorageService: FileStorageService,
    @Autowired
    private val tagService: TagService,
    @Autowired
    private val userService: UserService
) {

    @Transactional
    fun uploadPhoto(
        file: MultipartFile,
        authUser: Authentication,
        description: String?,
        tags: List<String>?,
        photoMetadataDto: PhotoMetadataDto?
    ): UploadResultDto {
        validateUserUpload(authUser)
        validateFileFormat(file)


        val originalImage = ImageIO.read(file.inputStream)
        val width = photoMetadataDto?.width ?: originalImage.width
        val height = photoMetadataDto?.height ?: originalImage.height
        val format = photoMetadataDto?.format
            ?: file.originalFilename?.getPhotoType()
            ?: PhotoFormat.JPG

        val transformedImage = applyImageTransformation(
            originalImage,
            width,
            height,
            format
        )

        val (photoKey, fileUri) = uploadToFileStorage(transformedImage, authUser, format)

        val createdTags = createTagsFromMetadata(tags ?: emptyList())

        val createdPhoto = Photo(
            key = photoKey,
            createdBy = authUser.name,
            tags = createdTags.toMutableSet(),
            description = description,
            url = fileUri.toURL().toString(),
            format = format,
            width = width,
            height = height
        )

        photoRepository.save(createdPhoto)
        userService.increaseDailyUpload(authUser)

        return UploadResultDto(photoKey, createdPhoto.uploadDate,)
    }

    private fun validateFileFormat(file: MultipartFile) {
        if (!file.contentType?.startsWith("image")!!) {
            throw WrongFileFormatException("File must be an image.")
        }
    }

    private fun validateUserUpload(authUser: Authentication) {
        val user = userService.findOrCreateUser(authUser)
        val userUploadLimit = user.subscriptionPlan.uploadLimit
        val userUploadCount = user.dailyUpload
        if (userUploadCount >= userUploadLimit) {
            throw DailyUploadReachedException("You reached upload limit for today")
        }
        if (!user.isProfileCompleted) {
            throw ProfileNotCompletedException("You need to complete your profile first")
        }
    }

    private fun applyImageTransformation(
        inputStream: BufferedImage,
        width: Int,
        height: Int,
        format: PhotoFormat,
    ): InputStream {
     return ImageTransformationService.Builder(inputStream)
            .width(width)
            .height(height)
            .format(format)
            .buildProcessedImage()
    }

    private fun uploadToFileStorage(
        fileStream: InputStream, authUser: Authentication, format: PhotoFormat
    ): Pair<String, URI> {
        val photoKey = "${authUser.name}/${UUID.randomUUID()}.${format.name}"
        val fileUri = fileStorageService.uploadFile(fileStream, photoKey)
        return fileUri
    }

    private fun createTagsFromMetadata(tags: List<String>): List<PhotoTag> {
        return tags.let { tagService.createTags(it.toSet()) }
    }

}