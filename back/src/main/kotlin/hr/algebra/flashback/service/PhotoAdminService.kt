package hr.algebra.flashback.service

import hr.algebra.flashback.dto.photo.PhotoDto
import hr.algebra.flashback.dto.photo.toDto
import hr.algebra.flashback.dto.upload.UpdatePhotoDto
import hr.algebra.flashback.exception.PhotoNotFoundException
import hr.algebra.flashback.repository.PhotoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class PhotoAdminService(
    @Autowired
    private val photoRepository: PhotoRepository,
    @Autowired
    private val tagService: TagService,
    @Autowired
    private val fileStorageService: FileStorageService
) {

    fun findPaginatedPhotos(
        pageable: Pageable,
        query: String?,
        fromDate: LocalDate?,
        toDate: LocalDate?
    ): Page<PhotoDto> {
        return photoRepository.findAllByQuery(pageable, query, fromDate, toDate).map { it.toDto() }
    }

    fun findById(photoId: Long): PhotoDto {
        return photoRepository.findById(photoId)
            .orElseThrow { PhotoNotFoundException("Photo with id: $photoId not found") }
            .toDto()
    }

    @Transactional
    fun updatePhotoMetadata(photoId: Long, updatePhotoDto: UpdatePhotoDto): PhotoDto {
        val photo = photoRepository.findById(photoId)
            .orElseThrow { PhotoNotFoundException("Photo with id: $photoId not found") }

        val tags = updatePhotoDto.tags?.let { tagService.createTags(it.toSet()) }

        photo.width = updatePhotoDto.width
        photo.height = updatePhotoDto.height
        photo.format = updatePhotoDto.format
        if (tags != null) {
            photo.tags = tags.toMutableSet()
        }
        photo.description = updatePhotoDto.description

        return photoRepository.save(photo).toDto()
    }

    @Transactional
    fun deletePhoto(photoId: Long) {
        val photo = photoRepository.findById(photoId)
            .orElseThrow { PhotoNotFoundException("Photo with id: $photoId not found") }

        fileStorageService.deleteFile(photo)
        photoRepository.delete(photo)
    }

    @Transactional
    fun deleteAllPhotosByUser(createdBy: String) {
        val photos = photoRepository.findByCreatedBy(createdBy)
        photos.forEach { fileStorageService.deleteFile(it) }
        photoRepository.deleteAll(photos)
    }

}