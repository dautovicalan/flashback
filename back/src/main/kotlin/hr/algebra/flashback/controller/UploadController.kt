package hr.algebra.flashback.controller

import hr.algebra.flashback.dto.upload.PhotoMetadataDto
import hr.algebra.flashback.dto.upload.UploadResultDto
import hr.algebra.flashback.model.log.LogAction
import hr.algebra.flashback.service.LogService
import hr.algebra.flashback.service.UploadService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/upload")
class UploadController(
    @Autowired
    private val uploadService: UploadService,
    @Autowired
    private val logService: LogService
) {
    @PostMapping("/", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE])
    fun uploadPhoto(
        @RequestPart("file") file: MultipartFile,
        @RequestPart("description", required = false) description: String?,
        @RequestPart("tags", required = false) tags: List<String>?,
        @RequestPart("photoMetadataDto", required = false) photoMetadataDto: PhotoMetadataDto?,
        authUser: Authentication
    ): ResponseEntity<UploadResultDto> {
        val uploadedPhotoDto = uploadService.uploadPhoto(
            file,
            description,
            tags,
            photoMetadataDto,
            authUser
        )
        logService.logAction(
            authUser.name,
            "Photo uploaded: ${uploadedPhotoDto.key}",
            LogAction.UPLOAD_PHOTO
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadedPhotoDto)
    }
}