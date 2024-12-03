package hr.algebra.flashback.service

import hr.algebra.flashback.dto.upload.UpdatePhotoMetadataDto
import hr.algebra.flashback.exception.NotOwnerOfPhotoException
import hr.algebra.flashback.exception.PhotoNotFoundException
import hr.algebra.flashback.handler.DescriptionUpdateHandler
import hr.algebra.flashback.handler.PhotoOwnershipValidationHandler
import hr.algebra.flashback.handler.UpdateHandler
import hr.algebra.flashback.model.upload.Photo
import hr.algebra.flashback.repository.PhotoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class PhotoService(
    @Autowired
    private val photoRepository: PhotoRepository,
    @Autowired
    private val fileStorageService: FileStorageService,
    @Autowired
    private val tagService: TagService
)  {

    fun findPhotos() = photoRepository.findAll().map { it }

    fun findPhotosPaginated(
        pageable: Pageable,
        query: String?,
        fromDate: LocalDate?,
        toDate: LocalDate?
    ): Page<Photo> {
        return photoRepository.findAllByQuery(
            pageable,
            query,
            fromDate,
            toDate
        ).map { it }
    }

    fun findPhotosByUserId(userId: String): List<Photo> {
        return photoRepository.findByCreatedBy(userId).map { it }
    }

    fun findById(photoId: Long): Photo {
        val photo = photoRepository.findById(photoId)
            .orElseThrow { PhotoNotFoundException("Photo with id $photoId not found") }
        return photo
    }

    @Transactional
    fun updatePhotoMetadata(photoId: Long, photoMetadataDto: UpdatePhotoMetadataDto, authUser: Authentication): Photo {
        val photo = photoRepository.findById(photoId)
            .orElseThrow { PhotoNotFoundException("Photo with id: $photoId not found") }

        // CHAIN OF RESPONSIBILITY
        val handlers = listOf<UpdateHandler<Photo, UpdatePhotoMetadataDto>>(
            PhotoOwnershipValidationHandler(),
            DescriptionUpdateHandler()
        )

        var updatedPhoto = photo
        handlers.forEach { updatedPhoto = it.handle(updatedPhoto, photoMetadataDto, authUser) }


        val tags = if (photoMetadataDto.tags != null) tagService.createTags(photoMetadataDto.tags.toSet()) else emptyList()
        updatedPhoto.tags = tags.toMutableSet()

        return photoRepository.save(updatedPhoto)
    }

    @Transactional
    fun deletePhoto(photoId: Long, authUser: Authentication) {
        val photo = photoRepository.findById(photoId)
            .orElseThrow { PhotoNotFoundException("Photo with id: $photoId not found") }

        if (photo.createdBy != authUser.name) {
            throw NotOwnerOfPhotoException("You are not the owner of this photo")
        }

        fileStorageService.deleteFile(photo)
        photoRepository.deleteById(photo.id)
    }

    @Transactional
    fun deleteAllPhotosByUser(authUser: Authentication) {
        val photos = photoRepository.findByCreatedBy(authUser.name)
        photos.forEach { fileStorageService.deleteFile(it) }
        photoRepository.deleteAll(photos)
    }

}