package hr.algebra.flashback.controller

import hr.algebra.flashback.dto.photo.PhotoDto
import hr.algebra.flashback.dto.photo.toDto
import hr.algebra.flashback.dto.upload.UpdatePhotoMetadataDto
import hr.algebra.flashback.model.log.LogAction
import hr.algebra.flashback.service.LogService
import hr.algebra.flashback.service.PhotoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/photos")
class PhotoController(
    @Autowired
    private val photoService: PhotoService,
    @Autowired
    private val logService: LogService
) {

    @GetMapping("/")
    fun getPaginatedPhotos(@RequestParam(defaultValue = "0") page: Int,
                           @RequestParam(defaultValue = "10") size: Int,
                           @RequestParam(defaultValue = "", required = false) query: String?,
                           @RequestParam(required = false) fromDate: LocalDate?,
                           @RequestParam(required = false) toDate: LocalDate?
    ): ResponseEntity<Page<PhotoDto>> {
        val pageable = PageRequest.of(page, size)
        val paginatedPhotos = photoService.findPhotosPaginated(pageable, query, fromDate, toDate)
        return ResponseEntity.ok(paginatedPhotos.map { it.toDto() })
    }

    @GetMapping("/user/{userId}")
    fun getPhotosByUser(@PathVariable userId: String): ResponseEntity<List<PhotoDto>> {
        val photos = photoService.findPhotosByUserId(userId)
        return ResponseEntity.ok(photos.map { it.toDto() })
    }

    @GetMapping("/{photoId}")
    fun getPhotoById(@PathVariable photoId: Long): ResponseEntity<PhotoDto> {
        val photo = photoService.findById(photoId)
        return ResponseEntity.ok(photo.toDto())
    }

    @PutMapping("/{photoId}")
    fun updatePhotoMetadata(@PathVariable photoId: Long,
                            @RequestBody photoMetadataData: UpdatePhotoMetadataDto,
                            authUser: Authentication
    ): ResponseEntity<PhotoDto> {
        val updatedPhoto = photoService.updatePhotoMetadata(photoId, photoMetadataData, authUser)
        logService.logAction(
            authUser.name,
            "Photo metadata updated: ${updatedPhoto.id}",
            LogAction.UPDATE_PHOTO
        )
        return ResponseEntity.ok(updatedPhoto.toDto())
    }

    @DeleteMapping("/{photoId}")
    fun deletePhoto(
        @PathVariable photoId: Long,
        authUser: Authentication
    ): ResponseEntity<Unit> {
        photoService.deletePhoto(photoId, authUser)
        logService.logAction(
            authUser.name,
            "Photo deleted: $photoId",
            LogAction.DELETE_PHOTO
        )
        return ResponseEntity.noContent().build()
    }
}